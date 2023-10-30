package callofproject.dev.repository.usermanagement.repository.rdbms;

import callofproject.dev.repository.usermanagement.entity.UserRate;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IUserRateRepository extends CrudRepository<UserRate, UUID>
{
}
