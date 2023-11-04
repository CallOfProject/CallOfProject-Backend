package callofproject.dev.repository.authentication.repository.rdbms;

import callofproject.dev.repository.authentication.entity.UserRate;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface IUserRateRepository extends CrudRepository<UserRate, UUID>
{
}
