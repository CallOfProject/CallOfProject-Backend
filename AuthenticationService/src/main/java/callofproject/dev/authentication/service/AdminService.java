package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.MessageResponseDTO;
import callofproject.dev.authentication.dto.MultipleMessageResponseDTO;
import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.mapper.IUserMapper;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import callofproject.dev.service.jwt.JwtUtil;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static java.time.LocalDate.now;
import static java.util.stream.StreamSupport.stream;

@Service
@Lazy
public class AdminService
{

    private final UserManagementServiceHelper m_managementServiceHelper;
    private final AuthenticationProvider m_authenticationProvider;
    private final IUserMapper m_userMapper;

    public AdminService(UserManagementServiceHelper managementServiceHelper, AuthenticationProvider authenticationProvider, IUserMapper userMapper)
    {
        m_managementServiceHelper = managementServiceHelper;
        m_authenticationProvider = authenticationProvider;
        m_userMapper = userMapper;
    }

    /**
     * Find all users pageable
     *
     * @param page represent the page
     * @return the UsersShowingAdminDTO
     */
    public MultipleMessageResponseDTO<UsersShowingAdminDTO> findAllUsersPageable(int page)
    {
        return doForDataService(() -> findAllUsersPageableCallback(page), "AdminService::findAllUsersPageable");
    }

    /**
     * Remove user with given username
     *
     * @param username represent the username
     * @return boolean value.
     */
    public MessageResponseDTO<Boolean> removeUser(String username)
    {
        return doForDataService(() -> removeUserCallback(username), "AdminService::removeUser");
    }

    /**
     * Find Users with given word. If username contains the word, return it.
     *
     * @param page represent the page
     * @param word represent the part of username
     * @return UsersShowingAdminDTO
     */
    public MultipleMessageResponseDTO<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCase(int page, String word)
    {
        return doForDataService(() -> findUsersByUsernameContainsIgnoreCaseCallback(page, word),
                "AdminService::findUsersByUsernameContainsIgnoreCase");
    }

    /**
     * Find Users with given word. If username not contains the word, return it.
     *
     * @param page represent the page
     * @param word represent the part of username
     * @return UsersShowingAdminDTO
     */
    public MultipleMessageResponseDTO<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCase(int page, String word)
    {
        return doForDataService(() -> findUsersByUsernameNotContainsIgnoreCaseCallback(page, word),
                "AdminService::findUsersByUsernameNotContainsIgnoreCase");
    }


    /**
     * Update user with given UserUpdateDTOAdmin class
     *
     * @param userUpdateDTO represent the updating information
     * @return UserShowingAdminDTO class.
     */
    public MessageResponseDTO<UserShowingAdminDTO> updateUser(UserUpdateDTOAdmin userUpdateDTO)
    {
        return doForDataService(() -> updateUserCallback(userUpdateDTO),
                "AdminService::findUsersByUsernameNotContainsIgnoreCase");
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
        return doForDataService(() -> m_managementServiceHelper.getUserServiceHelper().countUsersByCreationDateAfter(now().minusDays(day)),
                "AdminService::findNewUsersLastNday");
    }
    //-------------------------------------------CALLBACKS-------------------------------------------------------------

    private long getTotalPage()
    {
        return m_managementServiceHelper.getUserServiceHelper().getPageSize();
    }


    private MessageResponseDTO<UserShowingAdminDTO> updateUserCallback(UserUpdateDTOAdmin userUpdateDTO)
    {
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(userUpdateDTO.username());

        if (user.isEmpty())
            throw new DataServiceException("User not found!");

        if (user.get().getRoles().stream().map(Role::getName).anyMatch(roleName -> roleName.equals(RoleEnum.ROLE_ROOT.getRole())
                || roleName.equals(RoleEnum.ROLE_ADMIN.getRole())))
            throw new DataServiceException("You cannot edit this user!");

        user.get().setEmail(userUpdateDTO.email());
        user.get().setBirthDate(userUpdateDTO.birthDate());
        user.get().setFirstName(userUpdateDTO.firstName());
        user.get().setMiddleName(userUpdateDTO.middleName());
        user.get().setLastName(userUpdateDTO.lastName());
        user.get().setAccountBlocked(userUpdateDTO.isAccountBlocked());

        var savedUser = m_managementServiceHelper.getUserServiceHelper().saveUser(user.get());

        var userDto = m_userMapper.toUserShowingAdminDTO(savedUser);

        return new MessageResponseDTO<>("User updated successfully!", HttpStatus.SC_OK, userDto);
    }


    private MessageResponseDTO<Boolean> removeUserCallback(String username)
    {
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User Not Found!");

        if (user.get().isAdminOrRoot())
            throw new DataServiceException("You cannot remove this user!");

        m_managementServiceHelper.getUserServiceHelper().removeUser(user.get());

        return new MessageResponseDTO<>("User removed Successfully!", HttpStatus.SC_OK, true);
    }

    private MultipleMessageResponseDTO<UsersShowingAdminDTO> findAllUsersPageableCallback(int page)
    {

        var dtoList = m_userMapper.toUsersShowingAdminDTO(stream(m_managementServiceHelper.getUserServiceHelper()
                .findAllPageable(page).spliterator(), true)
                .map(m_userMapper::toUserShowingAdminDTO).toList());

        var msg = String.format("%d user found!", dtoList.users().size());

        return new MultipleMessageResponseDTO<>(getTotalPage(), page, dtoList.users().size(), msg, dtoList);
    }

    private MultipleMessageResponseDTO<UsersShowingAdminDTO> findUsersByUsernameContainsIgnoreCaseCallback(int page, String word)
    {
        var dtoList = m_userMapper.toUsersShowingAdminDTO(stream(m_managementServiceHelper.getUserServiceHelper()
                .findUsersByUsernameContainsIgnoreCase(word, page).spliterator(), true)
                .map(m_userMapper::toUserShowingAdminDTO).toList());

        var msg = String.format("%d user found!", dtoList.users().size());

        return new MultipleMessageResponseDTO<>(getTotalPage(), page, dtoList.users().size(), msg, dtoList);
    }

    private MultipleMessageResponseDTO<UsersShowingAdminDTO> findUsersByUsernameNotContainsIgnoreCaseCallback(int page, String word)
    {
        var dtoList = m_userMapper.toUsersShowingAdminDTO(stream(m_managementServiceHelper.getUserServiceHelper()
                .findUsersByUsernameNotContainsIgnoreCase(word, page).spliterator(), true)
                .map(m_userMapper::toUserShowingAdminDTO).toList());

        var msg = String.format("%d user found!", dtoList.users().size());

        return new MultipleMessageResponseDTO<>(getTotalPage(), page, dtoList.users().size(), msg, dtoList);
    }


    public Object authenticate(AuthenticationRequest request)
    {
        m_authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_managementServiceHelper.getUserServiceHelper().findByUsername(request.username());

        if (user.isEmpty())
            return new AuthenticationResponse(null, null, false);

        if (!user.get().isAdmin())
            throw new DataServiceException("You are not admin!");

        var authorities = JwtUtil.populateAuthorities(user.get().getRoles());
        var claims = new HashMap<String, Object>();
        claims.put("authorities", authorities);
        var jwtToken = JwtUtil.generateToken(claims, user.get().getUsername());
        var refreshToken = JwtUtil.generateRefreshToken(claims, user.get().getUsername());

        return new AuthenticationResponse(jwtToken, refreshToken, true);
    }
}