package callofproject.dev.authentication.test.controller;

import callofproject.dev.authentication.controller.AdminController;
import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.service.AdminService;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.repository.authentication.entity.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest
{
    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    @Test
    public void testLogin_whenGivenAuthenticationRequestDTO_thenReturnAuthenticationResponseDTO()
    {
        var mockRequest = new AuthenticationRequest("cop_root", "cop123");

        var userId = UUID.randomUUID();

        var expectedResponse = new AuthenticationResponse("token", "", false,
                "ROLE_ROOT", false, userId);

        when(adminService.authenticate(any(AuthenticationRequest.class))).thenReturn(expectedResponse);

        var response = adminController.authenticate(mockRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        var authenticationResponse = (AuthenticationResponse) response.getBody();
        assertEquals("token", authenticationResponse.getAccessToken());
        assertEquals("ROLE_ROOT", authenticationResponse.getRole());
        assertEquals(userId, authenticationResponse.getUser_id());
    }


    @Test
    public void testFindAllUserByPage_whenValidPage_thenReturnUserPage()
    {
        // Arrange
        int page = 1;
        var userId = UUID.randomUUID();

        var user1 = new UserShowingAdminDTO("username1", userId, Set.of(new Role("ROLE_USER")), "email1", false,
                "name1", "middleName", "surname1", LocalDate.now(), LocalDate.now());
        var user2 = new UserShowingAdminDTO("username2", userId, Set.of(new Role("ROLE_USER")), "email2", false,
                "name2", "middleName", "surname2", LocalDate.now(), LocalDate.now());

        var dto = new UsersShowingAdminDTO(List.of(user1, user2));
        var response = new MultipleResponseMessagePageable<>(1, 1, 2, "Found", dto);
        // Act
        when(adminService.findAllUsersPageable(page)).thenReturn(response);

        ResponseEntity<Object> expectedResponse = adminController.findAllUserByPage(page);


        // Assert
        assertEquals(HttpStatus.OK, expectedResponse.getStatusCode());
        var obj = (MultipleResponseMessagePageable<UsersShowingAdminDTO>) expectedResponse.getBody();
        assertNotNull(obj);
        assertEquals(obj.getObject().users().size(), 2);
        assertEquals(obj.getObject().users().get(0).username(), "username1");
    }
/*
    @Test
    public void testFindUsersByUsernameContainsIgnoreCase_whenValidParameters_thenReturnUsers()
    {
        // Arrange
        int page = 1;
        String word = "example";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(*//* Your expected response body here *//*);
        when(adminService.findUsersByUsernameContainsIgnoreCase(page, word)).thenReturn(expectedResponse.getBody());

        // Act
        ResponseEntity<Object> response = adminController.findUsersByUsernameContainsIgnoreCase(page, word);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testFindUsersByUsernameNotContainsIgnoreCase_whenValidParameters_thenReturnUsers()
    {
        // Arrange
        int page = 1;
        String word = "example";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(*//* Your expected response body here *//*);
        when(adminService.findUsersByUsernameNotContainsIgnoreCase(page, word)).thenReturn(expectedResponse.getBody());

        // Act
        ResponseEntity<Object> response = adminController.findUsersByUsernameNotContainsIgnoreCase(page, word);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testRemoveUserByUsername_whenValidUsername_thenReturnSuccess()
    {
        // Arrange
        String username = "example";
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(*//* Your expected response body here *//*);
        when(adminService.removeUser(username)).thenReturn(expectedResponse.getBody());

        // Act
        ResponseEntity<Object> response = adminController.removeUserByUsername(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testUpdateUserByUsername_whenValidUserUpdateDTO_thenReturnUpdatedUser()
    {
        // Arrange
        UserUpdateDTOAdmin userUpdateDTO = new UserUpdateDTOAdmin();
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(*//* Your expected response body here *//*);
        when(adminService.updateUser(userUpdateDTO)).thenReturn(expectedResponse.getBody());

        // Act
        ResponseEntity<Object> response = adminController.updateUserByUsername(userUpdateDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testFindAllUserCount_thenReturnTotalUserCount()
    {
        // Arrange
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(*//* Your expected response body here *//*);
        when(adminService.findAllUserCount()).thenReturn(expectedResponse.getBody());

        // Act
        ResponseEntity<Object> response = adminController.findAllUserCount();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testFindNewUserCount_whenValidDay_thenReturnNewUserCount()
    {
        // Arrange
        int day = 30;
        ResponseEntity<Object> expectedResponse = ResponseEntity.ok(*//* Your expected response body here *//*);
        when(adminService.findNewUsersLastNday(day)).thenReturn(expectedResponse.getBody());

        // Act
        ResponseEntity<Object> response = adminController.findNewUserCount(day);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        // Add more assertions as needed
    }

    @Test
    public void testAuthenticate_whenInvalidAuthenticationRequest_thenReturnErrorMessage()
    {
        // Arrange
        AuthenticationRequest request = new AuthenticationRequest("invalid_user", "invalid_password");
        ErrorMessage expectedError = new ErrorMessage("Invalid credentials", false, 401);
        when(adminService.authenticate(request)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.authenticate(request);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testFindAllUserByPage_whenInvalidPage_thenReturnErrorMessage()
    {
        // Arrange
        int page = -1; // Invalid page number
        ErrorMessage expectedError = new ErrorMessage("Invalid page number", false, 400);
        when(adminService.findAllUsersPageable(page)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.findAllUserByPage(page);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testFindUsersByUsernameContainsIgnoreCase_whenInvalidParameters_thenReturnErrorMessage()
    {
        // Arrange
        int page = 1;
        String word = null; // Invalid parameter
        ErrorMessage expectedError = new ErrorMessage("Invalid parameters", false, 400);
        when(adminService.findUsersByUsernameContainsIgnoreCase(page, word)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.findUsersByUsernameContainsIgnoreCase(page, word);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testFindUsersByUsernameNotContainsIgnoreCase_whenInvalidParameters_thenReturnErrorMessage()
    {
        // Arrange
        int page = -1; // Invalid page number
        String word = "example";
        ErrorMessage expectedError = new ErrorMessage("Invalid page number", false, 400);
        when(adminService.findUsersByUsernameNotContainsIgnoreCase(page, word)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.findUsersByUsernameNotContainsIgnoreCase(page, word);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testRemoveUserByUsername_whenInvalidUsername_thenReturnErrorMessage()
    {
        // Arrange
        String username = null; // Invalid username
        ErrorMessage expectedError = new ErrorMessage("Invalid username", false, 400);
        when(adminService.removeUser(username)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.removeUserByUsername(username);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testUpdateUserByUsername_whenInvalidUserUpdateDTO_thenReturnErrorMessage()
    {
        // Arrange
        UserUpdateDTOAdmin userUpdateDTO = null; // Invalid UserUpdateDTO
        ErrorMessage expectedError = new ErrorMessage("Invalid UserUpdateDTO", false, 400);
        when(adminService.updateUser(userUpdateDTO)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.updateUserByUsername(userUpdateDTO);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testFindAllUserCount_whenNoUsersFound_thenReturnErrorMessage()
    {
        // Arrange
        ErrorMessage expectedError = new ErrorMessage("Users Not Found!", false, 500);
        when(adminService.findAllUserCount()).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.findAllUserCount();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }

    @Test
    public void testFindNewUserCount_whenNoUsersFound_thenReturnErrorMessage()
    {
        // Arrange
        int day = 30;
        ErrorMessage expectedError = new ErrorMessage("Users Not Found!", false, 500);
        when(adminService.findNewUsersLastNday(day)).thenReturn(expectedError);

        // Act
        ResponseEntity<Object> response = adminController.findNewUserCount(day);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody() instanceof ErrorMessage);
        assertEquals(expectedError, response.getBody());
    }*/
}