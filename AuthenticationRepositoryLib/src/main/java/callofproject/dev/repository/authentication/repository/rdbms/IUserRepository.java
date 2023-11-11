package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.USER_REPOSITORY_BEAN;

@Repository(USER_REPOSITORY_BEAN)
@Lazy
public interface IUserRepository extends JpaRepository<User, UUID>
{
    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Page<User> findUsersByBirthDate(LocalDate localDate, Pageable pageable);

    Page<User> findUsersByBirthDateBetween(LocalDate start, LocalDate end, Pageable pageable);

    Page<User> findUsersByUsernameContainsIgnoreCase(String namePart, Pageable pageable);

    Page<User> findUsersByUsernameNotContainsIgnoreCase(String namePart, Pageable pageable);

    //Page<User> findUsersByUsernameNotContainsIgnoreCase(String namePart, Sort sort);

    Page<User> findUsersByCreationDate(LocalDate creationDate, Pageable pageable);

    Page<User> findUsersByCreationDateBetween(LocalDate start, LocalDate end, Pageable pageable);
}
