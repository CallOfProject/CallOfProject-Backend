package callofproject.dev.repository.authentication.repository.rdbms.security;

import callofproject.dev.repository.authentication.entity.security.Session;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.SESSION_REPOSITORY_BEAN;

@Repository(SESSION_REPOSITORY_BEAN)
@Lazy
public interface ISessionRepository extends CrudRepository<Session, UUID>
{
    Optional<Session> findSessionByToken(String token);
    Iterable<Session> findSessionsByExpireDateAfter(LocalDate expireDate);
}
