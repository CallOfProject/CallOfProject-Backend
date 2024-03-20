package callofproject.dev.task;

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
        entityManager.createNativeQuery("DELETE FROM USER_TASKS").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM TASK").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM PROJECT_PARTICIPANT").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM PROJECT").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_ROLES").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USERS").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM ROLES").executeUpdate();
    }
}