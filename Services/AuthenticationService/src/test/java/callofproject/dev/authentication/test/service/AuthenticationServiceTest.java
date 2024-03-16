package callofproject.dev.authentication.test.service;

import callofproject.dev.authentication.DatabaseCleaner;
import callofproject.dev.authentication.Injection;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {BASE_PACKAGE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class AuthenticationServiceTest
{
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    @Autowired
    private Injection m_injection;

    @Autowired
    private PasswordEncoder m_passwordEncoder;
    private User user1;
    private User user2;

    @BeforeEach
    public void setUpAndCheckUsers()
    {
        var user1 = new User(
                UUID.randomUUID(),
                "ahmetkoc",
                "Ahmet",
                "",
                "KOC",
                "ahmet@example.com",
                m_passwordEncoder.encode("12345"),
                LocalDate.of(1990, 5, 15),
                new Role("ROLE_USER")
        );
        user1.setAccountBlocked(true);

        var user2 = new User(
                UUID.randomUUID(),
                "emirkafadar",
                "Emir",
                "",
                "KAFADAR",
                "emir@example.com",
                m_passwordEncoder.encode("12345"),
                LocalDate.of(1990, 5, 15),
                new Role("ROLE_USER")
        );
        user1.setAccountBlocked(true);
        this.user1 = m_injection.getUserRepository().save(user1);
        this.user2 = m_injection.getUserRepository().save(user2);
    }


    @Test
    public void testLoginOperation_withGivenValidCredentials_shouldNotNull()
    {
        var loginRequest = new AuthenticationRequest(user2.getUsername(), "12345");
        var loginResponse = m_injection.getAuthenticationService().authenticateCallback(loginRequest);

        assertNotNull(loginResponse);
        assertEquals("ROLE_USER", loginResponse.getRole());
        assertNotNull(loginResponse.getAccessToken());
        assertTrue(loginResponse.isSuccess());
    }


    @Test
    public void testLoginOperation_withGivenInvalidCredentials_shouldThrowDataServiceException()
    {
        var loginRequest = new AuthenticationRequest("qwerty", "dsadsafs");

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAuthenticationService().authenticateCallback(loginRequest));
        assertEquals("Message: No user registered with this details! ", exception.getMessage());

    }


    @Test
    public void testLoginOperation_withGivenCorrectUsernameAndWrongPassword_shouldThrowDataServiceException()
    {
        var loginRequest = new AuthenticationRequest(user2.getUsername(), "password321");

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAuthenticationService().authenticateCallback(loginRequest));
        assertEquals("Message: Invalid password! ", exception.getMessage());
    }

    @Test
    public void testLoginOperation_withGivenWrongUsernameAndCorrectPassword_shouldThrowDataServiceException()
    {
        var loginRequest = new AuthenticationRequest("emirr", user2.getPassword());

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAuthenticationService().authenticateCallback(loginRequest));
        assertEquals("Message: No user registered with this details! ", exception.getMessage());
    }


    @Test
    public void testLoginOperation_withGivenEmptyUsernameAndPassword_shouldThrowDataServiceException()
    {
        var loginRequest = new AuthenticationRequest("", "");

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAuthenticationService().authenticateCallback(loginRequest));
        assertEquals("Message: No user registered with this details! ", exception.getMessage());
    }

    @Test
    public void testLoginOperation_withGivenBlockedUser_shouldThrowDataServiceException()
    {
        var loginRequest = new AuthenticationRequest(user1.getUsername(), "12345");

        var request = m_injection.getAuthenticationService().authenticateCallback(loginRequest);
        assertFalse(request.isSuccess());
        assertTrue(request.isBlocked());
    }

    @Test
    public void testLoginOperation_withGivenNullUser_shouldThrowDataServiceException()
    {

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAuthenticationService().authenticateCallback(null));
        assertEquals("Message: AuthenticationRequest is null! ", exception.getMessage());
    }

    @Test
    public void testRegisterOperation_withGivenValidRegisterDTO_shouldNotNull()
    {
        var user = new RegisterRequest("John", "Doe", "M", "john.doe", "john@example.com", "password123", LocalDate.of(1990, 5, 15));
        var dto = new UserSignUpRequestDTO(user.getEmail(), user.getFirst_name(),
                user.getMiddle_name(), user.getLast_name(), user.getUsername(),
                m_passwordEncoder.encode(user.getPassword()), user.getBirth_date(),
                RoleEnum.ROLE_USER);
        var registerResponse = m_injection.getUserManagementService().saveUserCallback(dto);

        assertNotNull(registerResponse);
        assertNotNull(registerResponse.getObject().accessToken());
        assertTrue(registerResponse.getObject().success());
    }


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
