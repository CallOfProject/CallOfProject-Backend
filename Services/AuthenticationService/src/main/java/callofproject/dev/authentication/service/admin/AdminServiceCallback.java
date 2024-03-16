package callofproject.dev.authentication.service.admin;

import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.UserKafkaDTO;
import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.mapper.IUserMapper;
import callofproject.dev.data.common.clas.MultipleResponseMessage;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import callofproject.dev.service.jwt.JwtUtil;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toList;
import static java.lang.String.format;
import static java.time.LocalDate.now;

@Service
@Lazy
public class AdminServiceCallback
{
    private final UserManagementServiceHelper m_managementServiceHelper;
    private final KafkaProducer m_kafkaProducer;
    private final AuthenticationProvider m_authenticationProvider;
    private final IUserMapper m_userMapper;

    public AdminServiceCallback(UserManagementServiceHelper managementServiceHelper, KafkaProducer kafkaProducer, AuthenticationProvider authenticationProvider, IUserMapper userMapper)
    {
        m_managementServiceHelper = managementServiceHelper;
        m_kafkaProducer = kafkaProducer;
        m_authenticationProvider = authenticationProvider;
        m_userMapper = userMapper;
    }

    /**
     * Find all users pageable
     *
     * @param page represent the page
     * @return the UsersShowingAdminDTO
     */
    public MultipleResponseMessagePageable<UsersShowingAdminDTO> findAllUsersPageable(int page)
    {
        var userListPageable = m_managementServiceHelper.getUserServiceHelper().findAllPageable(page);
        var dtoList = m_userMapper.toUsersShowingAdminDTO(toList(userListPageable.getContent(), m_userMapper::toUserShowingAdminDTO));
        var msg = format("%d user found!", dtoList.users().size());
        return new MultipleResponseMessagePageable<>(userListPageable.getTotalPages(), page, dtoList.users().size(), msg, dtoList);
    }

    /**
     * Find all users pageable
     *
     * @return the UsersShowingAdminDTO
     */
    public MultipleResponseMessage<UsersShowingAdminDTO> findAllUsers()
    {
        var users = m_managementServiceHelper.getUserServiceHelper().findAll();
        var dtoList = m_userMapper.toUsersShowingAdminDTO(toList(users, m_userMapper::toUserShowingAdminDTO));
        return new MultipleResponseMessage<>(dtoList.users().size(), format("%d user found!", dtoList.users().size()), dtoList);
    }


    /**
     * Remove user with given username
     *
     * @param username represent the username
     * @return boolean value.
     */
    public ResponseMessage<Boolean> removeUser(String username)
    {
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User Not Found!");

        if (user.get().isAdminOrRoot())
            throw new DataServiceException("You cannot remove this user!");

        user.get().setDeleteAt(LocalDateTime.now());
        m_managementServiceHelper.getUserServiceHelper().saveUser(user.get());

        return new ResponseMessage<>("User removed Successfully!", HttpStatus.SC_OK, true);
    }

    /**
     * Find Users with given word. If username contains the word, return it.
     *
     * @param page represent the page
     * @param word represent the part of username
     * @return UsersShowingAdminDTO
     */
    public MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCase(int page, String word)
    {
        var userListPageable = m_managementServiceHelper.getUserServiceHelper().findUsersByUsernameContainsIgnoreCase(word, page);
        var dtoList = m_userMapper.toUsersShowingAdminDTO(toList(userListPageable.getContent(), m_userMapper::toUserShowingAdminDTO));
        var msg = format("%d user found!", dtoList.users().size());
        return new MultipleResponseMessagePageable<>(userListPageable.getTotalPages(), page, dtoList.users().size(), msg, dtoList);
    }

    /**
     * Find Users with given word. If username not contains the word, return it.
     *
     * @param page represent the page
     * @param word represent the part of username
     * @return UsersShowingAdminDTO
     */
    public MultipleResponseMessagePageable<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCase(int page, String word)
    {
        var userListPageable = m_managementServiceHelper.getUserServiceHelper().findUsersByUsernameNotContainsIgnoreCase(word, page);
        var dtoList = m_userMapper.toUsersShowingAdminDTO(toList(userListPageable.getContent(), m_userMapper::toUserShowingAdminDTO));
        var msg = format("%d user found!", dtoList.users().size());
        return new MultipleResponseMessagePageable<>(userListPageable.getTotalPages(), page, dtoList.users().size(), msg, dtoList);
    }


    /**
     * Update user with given UserUpdateDTOAdmin class
     *
     * @param userUpdateDTO represent the updating information
     * @return UserShowingAdminDTO class.
     */
    public ResponseMessage<UserShowingAdminDTO> updateUser(UserUpdateDTOAdmin userUpdateDTO)
    {
        var authorizedPerson = m_managementServiceHelper.getUserServiceHelper().findById(UUID.fromString(userUpdateDTO.adminId()));
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(userUpdateDTO.username());

        // if user or authorized person not found
        if (user.isEmpty() || authorizedPerson.isEmpty())
            throw new DataServiceException("User not found!");

        // if authorized person is not admin
        if (!authorizedPerson.get().isAdminOrRoot())
            throw new DataServiceException("Permissions Denied!");

        // if user is admin or root
        var userRole = findTopRole(user.get());
        var authorizedPersonRole = findTopRole(authorizedPerson.get());

        var compareResult = compareRole(authorizedPersonRole, userRole);

        // if authorized person is not admin or root
        if (compareResult > 0)
            throw new DataServiceException("You cannot edit this user!");

        // Update user
        user.get().setEmail(userUpdateDTO.email());
        user.get().setBirthDate(userUpdateDTO.birthDate());
        user.get().setFirstName(userUpdateDTO.firstName());
        user.get().setMiddleName(userUpdateDTO.middleName());
        user.get().setLastName(userUpdateDTO.lastName());
        user.get().setAccountBlocked(userUpdateDTO.isAccountBlocked());

        // Save user
        var savedUser = m_managementServiceHelper.getUserServiceHelper().saveUser(user.get());

        // Convert to DTO
        var userDto = m_userMapper.toUserShowingAdminDTO(savedUser);

        return new ResponseMessage<>("User updated successfully!", HttpStatus.SC_OK, userDto);
    }


    /**
     * Authenticates a user based on their username and password.
     *
     * @param request The authentication request containing the user's credentials.
     * @return An AuthenticationResponse containing the JWT token and other authentication details.
     * @throws DataServiceException if the user is not an admin or if authentication fails.
     */
    public Object authenticate(AuthenticationRequest request)
    {
        m_authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(request.username());

        if (user.isEmpty())
            return new AuthenticationResponse(null, null, false, null, true, null);
        // if user is blocked or deleted
        if (user.get().getIsAccountBlocked() || user.get().getDeleteAt() != null)
            return new AuthenticationResponse(false, true);
        // if user is not admin
        if (!user.get().isAdmin())
            throw new DataServiceException("You are not admin!");

        // Token generation
        var authorities = JwtUtil.populateAuthorities(user.get().getRoles());
        var claims = new HashMap<String, Object>();
        claims.put("authorities", authorities);
        var jwtToken = JwtUtil.generateToken(claims, user.get().getUsername());
        var refreshToken = JwtUtil.generateRefreshToken(claims, user.get().getUsername());

        // Return the authentication response
        return new AuthenticationResponse(jwtToken, refreshToken, true, findTopRole(user.get()),
                user.get().getIsAccountBlocked(), user.get().getUserId());
    }

    /**
     * Find total user count.
     *
     * @return the total user count
     */
    public long findAllUserCount()
    {
        return doForDataService(() -> m_managementServiceHelper.getUserServiceHelper().count(), "AdminService::findAllUserCount");
    }

    /**
     * Find new users last n day.
     *
     * @param day represent the day
     * @return new user count.
     */
    public long findNewUsersLastNday(long day)
    {
        return doForDataService(() -> m_managementServiceHelper.getUserServiceHelper().countUsersByCreationDateAfter(now().minusDays(day)), "AdminService::findNewUsersLastNday");
    }

    /**
     * Publishes a user-related message to a Kafka topic.
     *
     * @param uuid      The UUID of the user.
     * @param operation The operation being performed on the user.
     */
    public void publishUser(UUID uuid, EOperation operation)
    {
        var savedUser = m_managementServiceHelper.getUserServiceHelper().findById(uuid);

        if (savedUser.isPresent())
        {
            var toProjectServiceDTO = new UserKafkaDTO(savedUser.get().getUserId(), savedUser.get().getUsername(), savedUser.get().getEmail(),
                    savedUser.get().getFirstName(), savedUser.get().getMiddleName(), savedUser.get().getLastName(), operation,
                    savedUser.get().getPassword(), savedUser.get().getRoles(),
                    savedUser.get().getDeleteAt(), 0, 0, 0);

            m_kafkaProducer.sendMessage(toProjectServiceDTO);
        }
    }

    /**
     * Publishes a user-related message to a Kafka topic based on username.
     *
     * @param username  The username of the user.
     * @param operation The operation being performed on the user.
     */
    public void publishUser(String username, EOperation operation)
    {
        var savedUser = m_managementServiceHelper.getUserServiceHelper().findByUsername(username);

        if (savedUser.isPresent())
        {
            var toProjectServiceDTO = new UserKafkaDTO(savedUser.get().getUserId(), savedUser.get().getUsername(), savedUser.get().getEmail(),
                    savedUser.get().getFirstName(), savedUser.get().getMiddleName(), savedUser.get().getLastName(), operation,
                    savedUser.get().getPassword(), savedUser.get().getRoles(),
                    savedUser.get().getDeleteAt(), 0, 0, 0);

            m_kafkaProducer.sendMessage(toProjectServiceDTO);
        }
    }


    /**
     * Finds the highest priority role of a user.
     *
     * @param user The user whose roles are to be evaluated.
     * @return The name of the top role of the user.
     */
    private String findTopRole(User user)
    {
        var role = RoleEnum.ROLE_USER.getRole();

        for (var r : user.getRoles())
        {
            if (r.getName().equals(RoleEnum.ROLE_ROOT.getRole()))
            {
                role = RoleEnum.ROLE_ROOT.getRole();
                break;
            }
            if (r.getName().equals(RoleEnum.ROLE_ADMIN.getRole()))
                role = RoleEnum.ROLE_ADMIN.getRole();
        }
        return role;
    }


    /**
     * Compares two roles based on a predefined order.
     *
     * @param role1 The first role to compare.
     * @param role2 The second role to compare.
     * @return An integer indicating the order of the roles.
     */
    private int compareRole(String role1, String role2)
    {
        var roleOrder = Arrays.asList(RoleEnum.ROLE_ROOT.getRole(), RoleEnum.ROLE_ADMIN.getRole(), RoleEnum.ROLE_USER.getRole());

        int index1 = roleOrder.indexOf(role1);
        int index2 = roleOrder.indexOf(role2);

        if (index1 == -1 || index2 == -1)
            return 0;

        return Integer.compare(index1, index2);
    }

}
