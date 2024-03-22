package callofproject.dev.community;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DatabaseCleaner
{
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void clearH2Database()
    {
        entityManager.createNativeQuery("DELETE FROM PROJECT_PARTICIPANT").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_CONNECTIONS").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM CONNECTION_REQUESTS").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM BLOCKED_CONNECTIONS").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM COMMUNITY_USERS").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM PROJECT").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_ROLES").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM COMMUNITY").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM ROLES").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USERS").executeUpdate();

    }
}