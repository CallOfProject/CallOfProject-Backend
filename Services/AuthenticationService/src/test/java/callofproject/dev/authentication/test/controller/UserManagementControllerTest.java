package callofproject.dev.authentication.test.controller;

import callofproject.dev.authentication.controller.UserManagementController;
import callofproject.dev.authentication.dto.UserDTO;
import callofproject.dev.authentication.dto.UserProfileUpdateDTO;
import callofproject.dev.authentication.dto.UserSaveDTO;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.user_profile.UserWithProfileDTO;
import callofproject.dev.authentication.service.usermanagement.UserManagementService;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.library.exception.service.DataServiceException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static callofproject.dev.authentication.Util.*;
import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.TEST_PROPERTIES_FILE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class UserManagementControllerTest
{
    @Mock
    private UserManagementService userManagementService;
    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private UserManagementController userManagementController;


    @Test
    public void testSaveUser_withValidData_shouldReturnSuccess()
    {
        // given
        var dto = createUserSignUpRequestDTO();
        var expectedResult = new ResponseMessage<>("User saved successfully!", 200, new UserSaveDTO("access-token", "token-refresh", true, UUID.randomUUID()));
        when(userManagementService.saveUser(any(UserSignUpRequestDTO.class))).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userManagementController.saveUser(dto);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    @Test
    public void testFindUserByUsername_withValidData_shouldReturnSuccess()
    {
        // given
        var username = "ahmetkoc";
        var expectedResult = new ResponseMessage<UserDTO>("User found successfully!", 200, createUserDTO());
        when(userManagementService.findUserByUsername(username)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userManagementController.findUserByUsername(username);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUserByUsername_withServiceException_shouldReturnInternalServerError()
    {
        // given
        var username = "nuricanozturk";
        when(userManagementService.findUserByUsername(username)).thenThrow(new DataServiceException("User not found!"));
        // Act
        ResponseEntity<Object> response = userManagementController.findUserByUsername(username);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testUpdateUserProfile_withServiceException_shouldReturnInternalServerError()
    {
        // given
        var dto = createUserProfileUpdateDTO();
        when(userManagementService.upsertUserProfile(any(UserProfileUpdateDTO.class), any(MultipartFile.class), any(MultipartFile.class))).thenThrow(new DataServiceException("Profile cannot be updated!"));
        // Act
        ResponseEntity<Object> response = userManagementController.updateUserProfile("dto", null, null);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testFindUserByWordContains_withValidData_shouldReturnSuccess()
    {
        // given
        String word = "ah";
        int page = 1;
        var expectedResult = new MultipleResponseMessagePageable<Object>(1, page, 1, "Found successfully!", createUsersDTO());
        when(userManagementService.findAllUsersPageableByContainsWord(page, word)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userManagementController.findUserByWordContains(word, page);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUserByWordContains_withServiceException_shouldReturnInternalServerError()
    {
        // given
        String word = "testWord";
        int page = 1;
        when(userManagementService.findAllUsersPageableByContainsWord(page, word)).thenThrow(new DataServiceException("Error finding users"));
        // Act
        ResponseEntity<Object> response = userManagementController.findUserByWordContains(word, page);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }


    @Test
    public void testFindUserProfileByUsername_withValidData_shouldReturnSuccess()
    {
        // given
        var username = "ahmetkoc";
        var expectedResult = new ResponseMessage<Object>("User with profile found!", 200, createUserProfileDTO());
        when(userManagementService.findUserProfileByUsername(username)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userManagementController.findUserProfileByUsername(username);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUserProfileByUsername_withServiceException_shouldReturnInternalServerError()
    {
        // given
        var username = "ahmetkoc";
        when(userManagementService.findUserProfileByUsername(username)).thenThrow(new DataServiceException("User profile not found"));
        // Act
        ResponseEntity<Object> response = userManagementController.findUserProfileByUsername(username);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testFindUserProfileByUserId_withValidData_shouldReturnSuccess()
    {
        // given
        var userId = UUID.randomUUID();
        var expectedResult = new ResponseMessage<Object>("User profile found!", 200, createUserProfileDTO());
        when(userManagementService.findUserProfileByUserId(userId)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userManagementController.findUserProfileByUserId(userId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUserProfileByUserId_withServiceException_shouldReturnInternalServerError()
    {
        // given
        var userId = UUID.randomUUID();
        when(userManagementService.findUserProfileByUserId(userId)).thenThrow(new DataServiceException("User profile not found"));
        // Act
        ResponseEntity<Object> response = userManagementController.findUserProfileByUserId(userId);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    public void testFindUserWithProfileByUserId_withValidData_shouldReturnSuccess()
    {
        // given
        var userId = UUID.randomUUID();
        var userWithProfile = new UserWithProfileDTO(userId, createUserDTO(), createUserProfileDTO());
        var expectedResult = new ResponseMessage<Object>("User with profile found!", 200, userWithProfile);
        when(userManagementService.findUserWithProfile(userId)).thenReturn(expectedResult);
        // Act
        ResponseEntity<Object> response = userManagementController.findUserWithProfileByUserId(userId);
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUserWithProfileByUserId_withServiceException_shouldReturnInternalServerError()
    {
        // given
        var userId = UUID.randomUUID();
        when(userManagementService.findUserWithProfile(userId)).thenThrow(new DataServiceException("Error finding user with profile"));
        // Act
        ResponseEntity<Object> response = userManagementController.findUserWithProfileByUserId(userId);
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}