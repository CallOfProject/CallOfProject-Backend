package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.usermanagement.BeanName.USER_REPOSITORY_BEAN;

@Repository(USER_REPOSITORY_BEAN)
@Lazy
public interface IUserRepository extends CrudRepository<User, UUID>
{
    Optional<User> findByEmail(String email);
}
