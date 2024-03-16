package callofproject.dev.authentication.test.controller;

import callofproject.dev.authentication.controller.AuthenticationController;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.service.authentication.AuthenticationService;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.TEST_PROPERTIES_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class AuthenticationControllerTest
{
    @Mock
    private AuthenticationService m_authenticationService;

    @InjectMocks
    private AuthenticationController m_authenticationController;

    private User user1;
    private User user2;


    @BeforeEach
    public void setUp()
    {
        user1 = new User(
                UUID.randomUUID(),
                "ahmetkoc",
                "Ahmet",
                "",
                "KOC",
                "ahmet@example.com",
                "12345",
                LocalDate.of(1990, 5, 15),
                new Role("ROLE_USER")
        );


        user2 = new User(
                UUID.randomUUID(),
                "ahmetkoc",
                "Ahmet",
                "",
                "KOC",
                "ahmet@example.com",
                "12345",
                LocalDate.of(1990, 5, 15),
                new Role("ROLE_USER")
        );
    }

    @Test
    public void testUserAuthenticate_withGivenValidUsernameAndPassword_shouldBeSuccess()
    {
        //given
        AuthenticationRequest request = new AuthenticationRequest(user1.getUsername(), user1.getPassword());
        var userId = UUID.randomUUID();
        when(m_authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse("token", "user", true, "ROLE_ADMIN", false, userId));

        // Act
        ResponseEntity<Object> response = m_authenticationController.authenticate(request);


        // Assert
        var result = (AuthenticationResponse) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("token", result.getAccessToken());
        assertEquals("ROLE_ADMIN", result.getRole());
        assertEquals(userId, result.getUser_id());
    }

    @Test
    public void testAuthenticate_whenInvalidAuthenticationRequest_thenReturnUnSuccess()
    {
        //given
        AuthenticationRequest request = new AuthenticationRequest("2143243", "password");

        when(m_authenticationService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse(null, null, false, null, false, null));

        // Act
        ResponseEntity<Object> response = m_authenticationController.authenticate(request);


        // Assert
        var result = (AuthenticationResponse) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(result.getAccessToken());
        assertNull(result.getRole());
        assertNull(result.getUser_id());
    }
}
