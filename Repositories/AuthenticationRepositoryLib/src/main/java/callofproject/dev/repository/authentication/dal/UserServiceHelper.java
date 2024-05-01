package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.repository.authentication.BeanName.USER_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.USER_REPOSITORY_BEAN;
import static org.springframework.data.domain.PageRequest.of;

@Component(USER_DAL_BEAN)
@Lazy
public class UserServiceHelper
{
    private final int m_defaultPageSize = 30;
    private final IUserRepository m_userRepository;

    public UserServiceHelper(@Qualifier(USER_REPOSITORY_BEAN) IUserRepository userRepository)
    {
        m_userRepository = userRepository;
    }

    public long count()
    {
        return doForRepository(() -> m_userRepository.count(), "UserRepository::count");
    }

    public long getPageSize()
    {
        return doForRepository(() -> ((m_userRepository.count() / m_defaultPageSize) + 1), "UserRepository::count");
    }


    public Iterable<User> saveAll(Iterable<User> users)
    {
        return doForRepository(() -> m_userRepository.saveAll(users), "UserRepository::saveAll");
    }

    public User saveUser(User user)
    {
        return doForRepository(() -> m_userRepository.save(user), "UserRepository::saveUser");
    }

    public void removeUser(User user)
    {
        doForRepository(() -> m_userRepository.delete(user), "UserRepository::removeUser");
    }

    public void removeUserById(UUID uuid)
    {
        doForRepository(() -> m_userRepository.deleteById(uuid), "UserRepository::removeUserById");
    }

    public Optional<User> findById(UUID id)
    {
        return doForRepository(() -> m_userRepository.findById(id), "UserRepository::findById");
    }

    public Iterable<User> findAll()
    {
        return doForRepository(() -> m_userRepository.findAll(), "UserRepository::findAll");
    }

    public Page<User> findAllPageable(int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findAll(pageable), "UserRepository::findAllPageable");
    }

    public Optional<User> findByEmail(String email)
    {
        return doForRepository(() -> m_userRepository.findByEmail(email), "UserRepository::findByEmail");
    }

    public Optional<User> findByUsername(String username)
    {
        return doForRepository(() -> m_userRepository.findByUsername(username), "UserRepository::findByUsername");
    }

    public void addNewRoleToUserByUsername(String username, Role role)
    {
        doForRepository(() -> findByUsername(username).ifPresent(user -> user.addRoleToUser(role)), "UserRepository::addNewRoleToUserByUsername");
    }

    public void addNewRoleToUserById(String uuid, Role role)
    {
        doForRepository(() -> findById(UUID.fromString(uuid)).ifPresent(user -> user.addRoleToUser(role)), "UserRepository::addNewRoleToUserById");
    }


    public Page<User> findUsersByBirthDate(LocalDate localDate, int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findUsersByBirthDate(localDate, pageable), "UserRepository::findUsersByBirthDate");
    }

    public Page<User> findUsersByBirthDateBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findUsersByBirthDateBetween(start, end, pageable), "UserRepository::findUsersByBirthDateBetween");
    }


    public Page<User> findUsersByUsernameNotContainsIgnoreCase(String namePart, int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findUsersByUsernameNotContainsIgnoreCase(namePart, pageable), "UserRepository::findUsersByUsernameNotContainsIgnoreCase");
    }


    public Page<User> findUsersByUsernameContainsIgnoreCase(String namePart, int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findUsersByUsernameContainsIgnoreCase(namePart, pageable), "UserRepository::findUsersByUsernameContainsIgnoreCase");
    }


    public Page<User> findUsersByCreationDate(LocalDate creationDate, int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findUsersByCreationDate(creationDate, pageable), "UserRepository::findUsersByCreationDate");
    }

    public Page<User> findUsersByCreationDateBetween(LocalDate start, LocalDate end, int page)
    {
        var pageable = of(page - 1, m_defaultPageSize);
        return doForRepository(() -> m_userRepository.findUsersByCreationDateBetween(start, end, pageable), "UserRepository::findUsersByCreationDateBetween");
    }


    public long countUsersByCreationDateAfter(LocalDate date)
    {
        return doForRepository(() -> m_userRepository.countUsersByCreationDateAfter(date), "UserRepository::countUsersByCreationDateAfter");
    }


    public Iterable<User> findAllByUserIds(List<UUID> userIds)
    {
        return doForRepository(() -> m_userRepository.findAllById(userIds), "UserRepository::findAllByUserIds");
    }
}