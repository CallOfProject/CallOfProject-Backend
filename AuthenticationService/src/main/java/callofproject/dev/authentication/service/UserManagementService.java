package callofproject.dev.authentication.service;


import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.dto.UserResponseDTO;
import callofproject.dev.repository.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.authentication.mapper.IUserMapper;
import callofproject.dev.repository.authentication.entity.nosql.MatchDB;
import callofproject.dev.repository.authentication.repository.nosql.IMatchDbRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;


@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService implements UserDetailsService
{
    private final UserManagementServiceHelper m_serviceHelper;
    private final IMatchDbRepository m_matchDbRepository;
    private final IUserMapper m_userMapper;

    public UserManagementService(@Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper, IMatchDbRepository matchDbRepository, IUserMapper userMapper)
    {
        m_serviceHelper = serviceHelper;
        m_matchDbRepository = matchDbRepository;
        m_userMapper = userMapper;
    }


    public UserResponseDTO<User> saveUser(UserSignUpRequestDTO userDTO) throws DataServiceException
    {
        try
        {
            var savedUser = m_serviceHelper.getUserServiceHelper().saveUser(m_userMapper.toUser(userDTO));

            if (savedUser == null)
                throw new DataServiceException("User cannot be saved!");

            return new UserResponseDTO<User>(true, savedUser);
        } catch (Exception exception)
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
            throw new DataServiceException("Internal Server Error!");
        }
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + username));

       /* Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());*/

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword());
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
