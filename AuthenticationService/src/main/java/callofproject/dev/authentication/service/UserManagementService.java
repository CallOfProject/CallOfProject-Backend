package callofproject.dev.authentication.service;


import callofproject.dev.authentication.dto.*;
import callofproject.dev.authentication.mapper.IUserMapper;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.repository.nosql.IMatchDbRepository;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;
import static java.lang.String.format;
import static java.util.stream.StreamSupport.stream;


@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService
{

    private final UserManagementServiceHelper m_serviceHelper;
    private final IMatchDbRepository m_matchDbRepository;
    private final IUserMapper m_userMapper;

    public UserManagementService(@Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper,
                                 IMatchDbRepository matchDbRepository,
                                 IUserMapper userMapper)
    {
        m_serviceHelper = serviceHelper;
        m_matchDbRepository = matchDbRepository;
        m_userMapper = userMapper;
    }

    /**
     * Save User with given dto class.
     *
     * @param userDTO represent the dto class
     * @return UserSaveDTO.
     */
    public UserSaveDTO saveUser(UserSignUpRequestDTO userDTO)
    {
        return doForDataService(() -> saveUserCallback(userDTO), "User cannot be saved!");
    }

    /**
     * Find user with given username
     *
     * @param username represent the username.
     * @return UserDTO class.
     */
    public UserDTO findUserByUsername(String username)
    {
        return doForDataService(() -> findUserByUsernameCallback(username), "User does not exists!");
    }

    /**
     * Find User with given username but returns the user entity.
     *
     * @param username represent the username.
     * @return User class.
     */
    public UserResponseDTO<User> findUserByUsernameForAuthenticationService(String username)
    {
        return doForDataService(() -> findUserByUsernameForAuthenticationServiceCallback(username), "User does not exists!");
    }

    /**
     * Find all users with given word and page.
     *
     * @param page represent the page.
     * @param word represent the containing word.
     * @return UsersDTO class.
     */
    public MultipleMessageResponseDTO<UsersDTO> findAllUsersPageableByContainsWord(int page, String word)
    {
        return doForDataService(() -> findAllUsersPageableByContainsWordCallback(page, word), "UserManagementService::findAllUsersPageableByContainsWord");
    }

    //-----------------------------------------------------CALLBACK-----------------------------------------------------
    public Iterable<User> saveUsers(List<UserSignUpRequestDTO> userDTOs) throws DataServiceException
    {
        try
        {
            var list = new ArrayList<User>();
            for (var userDTO : userDTOs)
            {
                var user = m_userMapper.toUser(userDTO);
                list.add(user);
            }

            return m_serviceHelper.getUserServiceHelper().saveAll(list);
        } catch (Exception exception)
        {
            return Collections.emptyList();
        }
    }


    private long getTotalPage()
    {
        return m_serviceHelper.getUserServiceHelper().getPageSize();
    }


    public UserSaveDTO saveUserCallback(UserSignUpRequestDTO userDTO)
    {
        var user = m_userMapper.toUser(userDTO);

        var savedUser = m_serviceHelper.getUserServiceHelper().saveUser(user);

        if (savedUser == null)
            throw new DataServiceException("User cannot be saved!");

        var claims = new HashMap<String, Object>();

        var authorities = JwtUtil.populateAuthorities(user.getRoles());
        claims.put("authorities", authorities);

        var token = JwtUtil.generateToken(claims, user.getUsername());
        var refreshToken = JwtUtil.generateToken(claims, user.getUsername());
        return new UserSaveDTO(token, refreshToken, true, savedUser.getUserId());
    }


    public UserDTO findUserByUsernameCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists");

        return m_userMapper.toUserDTO(user.get());
    }

    public UserResponseDTO<User> findUserByUsernameForAuthenticationServiceCallback(String username)
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

        if (user.isEmpty())
            throw new DataServiceException("User does not exists");

        return new UserResponseDTO<User>(true, user.get());
    }

    public MultipleMessageResponseDTO<UsersDTO> findAllUsersPageableByContainsWordCallback(int page, String word)
    {
        var dtoList = m_userMapper
                .toUsersDTO(stream(m_serviceHelper.getUserServiceHelper()
                        .findUsersByUsernameContainsIgnoreCase(word, page).spliterator(), true)
                        .map(m_userMapper::toUserDTO)
                        .toList());

        var msg = format("%d user found!", dtoList.users().size());

        return new MultipleMessageResponseDTO<>(getTotalPage(), page, dtoList.users().size(), msg, dtoList);
    }
}
