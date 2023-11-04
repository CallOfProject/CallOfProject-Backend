package callofproject.dev.authentication.service;

import callofproject.dev.library.constant.dto.usermanagement.UserResponseDTO;
import callofproject.dev.library.constant.dto.usermanagement.UserSignUpRequestDTO;
import callofproject.dev.library.constant.exception.CopServiceException;
import callofproject.dev.repository.authentication.dal.UserManagementServiceHelper;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.authentication.mapper.IUserMapper;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.repository.authentication.BeanName.USER_MANAGEMENT_DAL_BEAN;


@Service(USER_MANAGEMENT_SERVICE)
@Lazy
public class UserManagementService implements UserDetailsService
{
    private final UserManagementServiceHelper m_serviceHelper;
    private final IUserMapper m_userMapper;

    public UserManagementService(@Qualifier(USER_MANAGEMENT_DAL_BEAN) UserManagementServiceHelper serviceHelper, IUserMapper userMapper)
    {
        m_serviceHelper = serviceHelper;
        m_userMapper = userMapper;
    }


    public UserResponseDTO<User> saveUser(UserSignUpRequestDTO userDTO) throws CopServiceException
    {
        try
        {
            var savedUser = m_serviceHelper.getUserServiceHelper().saveUser(m_userMapper.toUser(userDTO));

            if (savedUser == null)
                throw new CopServiceException("User cannot be saved!", HttpStatus.SC_INTERNAL_SERVER_ERROR);

            return new UserResponseDTO<User>(true, savedUser);
        } catch (Exception exception)
        {
            throw new CopServiceException("User cannot be saved!", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    public UserResponseDTO<User> findUserByUsername(String username) throws CopServiceException
    {
        try
        {
            var user = m_serviceHelper.getUserServiceHelper().findByUsername(username);
            if (user.isEmpty())
                throw new CopServiceException("User does not exists", HttpStatus.SC_NO_CONTENT);

            return new UserResponseDTO<User>(true, user.get());
        } catch (CopServiceException exception)
        {
            throw new CopServiceException("Internal Server Error!", HttpStatus.SC_INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        var user = m_serviceHelper.getUserServiceHelper().findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + username));

        Set<GrantedAuthority> authorities = user
                .getRoles()
                .stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(user.getEmail(),
                user.getPassword(),
                authorities);
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
