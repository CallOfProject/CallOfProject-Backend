package callofproject.dev.authentication;

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
        entityManager.createNativeQuery("DELETE FROM USER_PROFILES_TO_LINK").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_PROFILES_TO_EXPERIENCE").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_PROFILES_TO_EDUCATION").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_PROFILES_TO_COURSE").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_ROLES").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM COP_USER").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM LINK").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM EXPERIENCE").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM EDUCATION").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM COURSE").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM COURSE_ORGANIZATION").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM ROLES").executeUpdate();
        entityManager.createNativeQuery("DELETE FROM USER_PROFILE").executeUpdate();
    }
}