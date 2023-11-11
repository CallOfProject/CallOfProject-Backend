package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import callofproject.dev.repository.authentication.repository.rdbms.IRoleRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.*;

@Component(USER_DAL_BEAN)
@Lazy
public class UserServiceHelper
{
    private final IUserRepository m_userRepository;


    public UserServiceHelper(@Qualifier(USER_REPOSITORY_BEAN) IUserRepository userRepository)
    {
        m_userRepository = userRepository;
    }

    public Iterable<User> saveAll(Iterable<User> users)
    {
        return m_userRepository.saveAll(users);
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

    public Iterable<User> findAllPageable(Pageable pageable)
    {
        return m_userRepository.findAll(pageable);
    }

    public Optional<User> findByEmail(String email)
    {
        return m_userRepository.findByEmail(email);
    }

    public Optional<User> findByUsername(String username)
    {
        return m_userRepository.findByUsername(username);
    }

    public void addNewRoleToUserByUsername(String username, Role role)
    {
        findByUsername(username).ifPresent(user -> user.addRoleToUser(role));
    }

    public void addNewRoleToUserById(String uuid, Role role)
    {
        findById(UUID.fromString(uuid)).ifPresent(user -> user.addRoleToUser(role));
    }


    public Iterable<User> findUsersByBirthDate(LocalDate localDate, Pageable pageable)
    {
        return m_userRepository.findUsersByBirthDate(localDate, pageable);
    }

    public Iterable<User> findUsersByBirthDateBetween(LocalDate start, LocalDate end, Pageable pageable)
    {
        return m_userRepository.findUsersByBirthDateBetween(start, end, pageable);
    }


    public Iterable<User> findUsersByUsernameNotContainsIgnoreCase(String namePart, Pageable pageable)
    {
        return m_userRepository.findUsersByUsernameNotContainsIgnoreCase(namePart, pageable);
    }


    public Iterable<User> findUsersByUsernameContainsIgnoreCase(String namePart, Pageable pageable)
    {
        return m_userRepository.findUsersByUsernameContainsIgnoreCase(namePart, pageable);
    }

 /*   public Iterable<User> findUsersByUsernameNotContainsIgnoreCase(String namePart, Sort sort)
    {
        return m_userRepository.findUsersByUsernameNotContainsIgnoreCase(namePart, sort);
    }*/


    public Iterable<User> findUsersByCreationDate(LocalDate creationDate, Pageable pageable)
    {
        return m_userRepository.findUsersByCreationDate(creationDate, pageable);
    }

    public Iterable<User> findUsersByCreationDateBetween(LocalDate start, LocalDate end, Pageable pageable)
    {
        return m_userRepository.findUsersByCreationDateBetween(start, end, pageable);
    }
}