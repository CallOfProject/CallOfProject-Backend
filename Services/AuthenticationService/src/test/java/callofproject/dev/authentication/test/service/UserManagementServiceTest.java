package callofproject.dev.authentication.test.service;

import callofproject.dev.authentication.DatabaseCleaner;
import callofproject.dev.authentication.Injection;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.library.exception.service.DataServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.authentication.util.Util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {BASE_PACKAGE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class UserManagementServiceTest
{
    @Autowired
    private Injection m_injection;

    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    private UUID user1Id;
    private UUID user2Id;

    @BeforeEach
    public void setUpAndCheckUsers()
    {
        var user1 = new UserSignUpRequestDTO(
                "can@example.com",
                "Nuri",
                "Can",
                "OZTURK",
                "canozturk",
                "password123",
                LocalDate.of(1990, 5, 15)
        );
        var user2 = new UserSignUpRequestDTO(
                "halil@example.com",
                "Halil",
                "Can",
                "OZTURK",
                "halil",
                "securePassword",
                LocalDate.of(1985, 9, 8)
        );

        var savedUser1 = m_injection.getUserManagementService().saveUserCallback(user1);
        var savedUser2 = m_injection.getUserManagementService().saveUserCallback(user2);

        assertNotNull(savedUser1);
        assertNotNull(savedUser2);

        user1Id = savedUser1.getObject().userId();
        user2Id = savedUser2.getObject().userId();
    }

    @AfterEach
    public void tearDown()
    {
        System.out.println("cleaning up");
        m_databaseCleaner.clearH2Database();
    }


    @Test
    public void testFindUserByUserId_withGivenUserId_shouldNotNull()
    {
        var user1 = m_injection.getUserManagementService().getUserIfExists(user1Id);
        var user2 = m_injection.getUserManagementService().getUserIfExists(user2Id);

        assertNotNull(user1);
        assertNotNull(user2);
    }


    @Test
    public void testFindUserByUserId_withGivenUserId_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getUserManagementService().getUserIfExists(UUID.randomUUID()));

        assertEquals("Message: User does not exists! ", exception.getMessage());
    }

    @Test
    public void testFindUserByUsername_withGivenUsername_shouldNotNull()
    {
        var user1 = m_injection.getUserManagementService().findUserByUsernameCallback("canozturk");
        var user2 = m_injection.getUserManagementService().findUserByUsernameCallback("halil");

        assertNotNull(user1);
        assertNotNull(user2);
    }

    @Test
    public void testFindUserByUsername_withGivenUsername_shouldBeNull()
    {
        var user = m_injection.getUserManagementService().findUserByUsernameCallback("no_name");

        assertNotEquals(user.getStatusCode(), 200);
        assertNull(user.getObject());
    }

    @Test
    public void testFindUserProfileByUserName_withGivenUsername_shouldNotNull()
    {
        var user = m_injection.getUserManagementService().findUserProfileByUsername("canozturk");

        assertNotNull(user);
        assertNotNull(user.getObject());
        assertEquals(user.getStatusCode(), 200);
    }

    @Test
    public void testFindUserProfileByUserName_withGivenUsername_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getUserManagementService().findUserProfileByUsername("no_name"));

        assertEquals("Message: UserManagementService::findUserProfileByUserId , Cause Message:Message: User does not exists! ", exception.getMessage());
    }

    @Test
    public void testFindUserProfileByUserId_withGivenUserId_shouldNotNull()
    {
        var user = m_injection.getUserManagementService().findUserProfileByUserId(user1Id);

        assertNotNull(user);
        assertNotNull(user.getObject());
        assertEquals(user.getStatusCode(), 200);
    }

    @Test
    public void testFindUserProfileByUserId_withGivenUserId_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getUserManagementService().findUserProfileByUserId(UUID.randomUUID()));

        assertEquals("Message: UserManagementService::findUserProfileByUserId , Cause Message:Message: User does not exists! "
                , exception.getMessage());
    }

    @Test
    public void testFindUserWithProfileByUserId_withGivenUserId_shouldNotNull()
    {
        var user = m_injection.getUserManagementService().findUserWithProfile(user1Id);

        assertNotNull(user);
        assertNotNull(user.getObject());
        assertEquals(user.getStatusCode(), 200);
    }

    @Test
    public void testFindUserWithProfileByUserId_withGivenUserId_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getUserManagementService().findUserWithProfile(UUID.randomUUID()));

        assertEquals("Message: UserManagementService::findUserWithProfile , Cause Message:Message: User does not exists! ", exception.getMessage());
    }
}
