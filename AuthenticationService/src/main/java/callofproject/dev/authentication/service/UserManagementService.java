package callofproject.dev.authentication.service;


import callofproject.dev.authentication.dto.UserResponseDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.BeanName;
import callofproject.dev.repository.authentication.dal.RoleServiceHelper;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.authentication.mapper.IUserMapper;
import callofproject.dev.repository.authentication.repository.nosql.IMatchDbRepository;
import callofproject.dev.repository.authentication.repository.rdbms.IRoleRepository;
import callofproject.dev.service.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;


@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService
{
    private final UserManagementServiceHelper m_serviceHelper;
    private final IMatchDbRepository m_matchDbRepository;
    private final IUserMapper m_userMapper;
    private final RoleServiceHelper m_roleRepository;

    public UserManagementService(@Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper,
                                 IMatchDbRepository matchDbRepository, IUserMapper userMapper,
                                 @Qualifier(BeanName.ROLE_DAL_BEAN) RoleServiceHelper roleRepository)
    {
        m_serviceHelper = serviceHelper;
        m_matchDbRepository = matchDbRepository;
        m_userMapper = userMapper;
        m_roleRepository = roleRepository;
    }


    public UserResponseDTO<User> saveUser(UserSignUpRequestDTO userDTO) throws DataServiceException
    {
        try
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

            return new UserResponseDTO<User>(true, token, refreshToken);
        }
        catch (Exception exception)
        {
            throw new DataServiceException("User cannot be saved!");
        }
    }

    public UserResponseDTO<User> findUserByUsername(String username)
    {
        try
        {
            var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);

            if (user.isEmpty())
                throw new DataServiceException("User does not exists");

            return new UserResponseDTO<User>(true, user.get());
        } catch (DataServiceException exception)
        {
            throw new DataServiceException(exception.getMessage());
        }
    }



   /* public void removeUser(User user)
    {
        m_userRepository.delete(user);
    }

    public void removeUserById(UUID uuid)
    {
        m_userRepository.deleteById(uuid);
    }

    public Optional<User> findById(UUID id)
    {
        return m_userRepository.findById(id);
    }

    public Iterable<User> findAll()
    {
        return m_userRepository.findAll();
    }

    public Optional<User> findByEmail(String email)
    {
        return m_userRepository.findByEmail(email);
    }
*/

}
