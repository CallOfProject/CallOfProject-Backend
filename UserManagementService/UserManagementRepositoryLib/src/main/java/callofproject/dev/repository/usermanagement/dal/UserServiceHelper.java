package callofproject.dev.repository.usermanagement.dal;

import callofproject.dev.repository.usermanagement.entity.User;
import callofproject.dev.repository.usermanagement.repository.rdbms.IUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.usermanagement.BeanName.USER_DAL_BEAN;
import static callofproject.dev.repository.usermanagement.BeanName.USER_REPOSITORY_BEAN;

@Component(USER_DAL_BEAN)
@Lazy
public class UserServiceHelper
{
    private final IUserRepository m_userRepository;

    public UserServiceHelper(@Qualifier(USER_REPOSITORY_BEAN) IUserRepository userRepository)
    {
        m_userRepository = userRepository;
    }

    public User saveUser(User user)
    {
        return m_userRepository.save(user);
    }

    public void removeUser(User user)
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
}
