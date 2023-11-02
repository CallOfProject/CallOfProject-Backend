package callofproject.dev.authentication.repository;


import callofproject.dev.authentication.entity.Session;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository("cop.session")
@Lazy
public interface ISessionRepository extends CrudRepository<Session, UUID>
{
}
