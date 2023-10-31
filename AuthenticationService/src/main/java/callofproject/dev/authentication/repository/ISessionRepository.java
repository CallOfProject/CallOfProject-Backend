package callofproject.dev.authentication.repository;

import callofproject.dev.authentication.entity.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface ISessionRepository extends CrudRepository<Session, UUID>
{
}
