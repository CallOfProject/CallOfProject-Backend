package callofproject.dev.repository.authentication.dal;

import callofproject.dev.repository.authentication.entity.security.Session;
import callofproject.dev.repository.authentication.repository.rdbms.security.ISessionRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static callofproject.dev.repository.authentication.BeanName.SESSION_DAL_BEAN;
import static callofproject.dev.repository.authentication.BeanName.SESSION_REPOSITORY_BEAN;

@Service(SESSION_DAL_BEAN)
@Lazy
public class SessionServiceHelper
{
    private final ISessionRepository m_sessionRepository;

    public SessionServiceHelper(@Qualifier(SESSION_REPOSITORY_BEAN) ISessionRepository sessionRepository)
    {
        m_sessionRepository = sessionRepository;
    }

    public Session saveSession(Session session)
    {
        return m_sessionRepository.save(session);
    }

    public void removeSessionById(UUID sessionId)
    {
        m_sessionRepository.deleteById(sessionId);
    }

    public Optional<Session> findSessionByToken(String token)
    {
        return m_sessionRepository.findSessionByToken(token);
    }

    public Optional<Session> findSessionById(UUID uuid)
    {
        return m_sessionRepository.findById(uuid);
    }

    public Iterable<Session> findSessionsByExpireDateAfter(LocalDate expireDate)
    {
        return m_sessionRepository.findSessionsByExpireDateAfter(expireDate);
    }
}
