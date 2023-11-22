package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.UserRate;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.USER_RATE_REPOSITORY_BEAN;

@Repository(USER_RATE_REPOSITORY_BEAN)
@Lazy
public interface IUserRateRepository extends CrudRepository<UserRate, UUID>
{
}
