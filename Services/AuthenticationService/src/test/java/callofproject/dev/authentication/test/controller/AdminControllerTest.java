package callofproject.dev.authentication.test.controller;

import callofproject.dev.authentication.controller.AdminController;
import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.service.AdminService;
import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdminControllerTest
{
    @Mock
    private AdminService adminService;
    @InjectMocks
    private AdminController m_adminController;

    private UsersShowingAdminDTO m_usersShowingAdminDTO;

    private User admin;

    private void provideDTO()
    {
        UserShowingAdminDTO user1 = new UserShowingAdminDTO(
                "ahmetkoc",
                UUID.randomUUID(),
                Set.of(new Role("ROLE_USER")),
                "ahmet@example.com",
                false,
                "Ahmet",
                "",
                "KOC",
                LocalDate.of(1990, 5, 15),
                LocalDateTime.now(),
                LocalDate.of(1985, 3, 20)
        );


        UserShowingAdminDTO user2 = new UserShowingAdminDTO(
                "emirkafadar",
                UUID.randomUUID(),
                Set.of(new Role("ROLE_USER")),
                "emir@example.com",
                true,
                "Emir",
                "",
                "KAFADAR",
                LocalDate.of(1988, 8, 10),
                LocalDateTime.now(),
                LocalDate.of(1992, 7, 5)
        );

        m_usersShowingAdminDTO = new UsersShowingAdminDTO(Stream.of(user1, user2).collect(Collectors.toList()));
    }

    @BeforeEach
    public void setUp()
    {
        provideDTO();

        admin = new User("cop_admin", "admin", "admin", "admin", "nuricanozturk01@gmail.com",
                "cop123", LocalDate.now(), new Role(RoleEnum.ROLE_ADMIN.getRole()));
        admin.setUserProfile(new UserProfile());
        admin.setUserId(UUID.randomUUID());
        admin.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
        var profile2 = new UserProfile();
        profile2.setUserProfileId(UUID.randomUUID());
        profile2.setUser(admin);
        admin.setUserProfile(profile2);
        m_usersShowingAdminDTO.users().add(new UserShowingAdminDTO(
                admin.getUsername(),
                admin.getUserId(),
                admin.getRoles(),
                admin.getEmail(),
                admin.isAccountBlocked(),
                admin.getFirstName(),
                admin.getMiddleName(),
                admin.getLastName(),
                admin.getCreationDate(),
                admin.getDeleteAt(),
                admin.getBirthDate()));

    }

    @Test
    public void testAuthenticate_withGivenValidUsernameAndPassword_shouldBeSuccess()
    {
        //given
        AuthenticationRequest request = new AuthenticationRequest(admin.getUsername(), admin.getPassword());
        var userId = UUID.randomUUID();
        when(adminService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse("token", "user", true, "ROLE_ADMIN", false, userId));

        // Act
        ResponseEntity<Object> response = m_adminController.authenticate(request);


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
        AuthenticationRequest request = new AuthenticationRequest("user1", "password");
        var userId = UUID.randomUUID();
        when(adminService.authenticate(any(AuthenticationRequest.class)))
                .thenReturn(new AuthenticationResponse(null, null, false, null, false, null));

        // Act
        ResponseEntity<Object> response = m_adminController.authenticate(request);


        // Assert
        var result = (AuthenticationResponse) response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(result.getAccessToken());
        assertNull(result.getRole());
        assertNull(result.getUser_id());
    }

    @Test
    public void testFindAllUserByPage_whenValidPage_thenReturnUserPage()
    {
        //Given
        var page = 1;

        var resultDto = new MultipleResponseMessagePageable<UsersShowingAdminDTO>(1, 1, 3, "Found Successfully!", m_usersShowingAdminDTO);

        when(adminService.findAllUsersPageable(page)).thenReturn(resultDto);
        //Act
        var response = m_adminController.findAllUserByPage(page);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUsersByUsernameContainsIgnoreCase_whenValidParameters_thenReturnUsers()
    {
        //Given
        var page = 1;
        var word = "a";

        var resultDto = new MultipleResponseMessagePageable<UsersShowingAdminDTO>(1, 1, 3, "Found Successfully!", m_usersShowingAdminDTO);

        when(adminService.findUsersByUsernameContainsIgnoreCase(page, word)).thenReturn(resultDto);
        //Act
        var response = m_adminController.findUsersByUsernameContainsIgnoreCase(page, word);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testFindUsersByUsernameNotContainsIgnoreCase_whenValidParameters_thenReturnUsers()
    {
        //Given
        var page = 1;
        var word = "xyz";


        var resultDto = new MultipleResponseMessagePageable<>(1, 1, 3, "Found Successfully!", m_usersShowingAdminDTO);

        when(adminService.findUsersByUsernameNotContainsIgnoreCase(page, word)).thenReturn(resultDto);
        //Act
        var response = m_adminController.findUsersByUsernameNotContainsIgnoreCase(page, word);

        //Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testRemoveUserByUsername_whenValidUsername_thenReturnSuccess()
    {
        //Given
        var username = "ahmetkoc";
        var resultDto = new ResponseMessage<Boolean>("User removed successfully!", 200, true);

        when(adminService.removeUser(username)).thenReturn(resultDto);
        //Act
        var response = m_adminController.removeUserByUsername(username);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testUpdateUserByUsername_whenValidUserUpdateDTO_thenReturnUpdatedUser()
    {
        var userUpdateDTO = new UserUpdateDTOAdmin(admin.getUserId().toString(), "ahmetkoc", "Nuri", "Can", "OZTURK", "cccan@mail.com",
                true, LocalDate.of(1990, 5, 15));
        UserShowingAdminDTO updatedUser = new UserShowingAdminDTO(
                "ahmetkoc",
                m_usersShowingAdminDTO.users().get(0).userId(),
                Set.of(new Role("ROLE_USER")),
                "cccan@mail.com",
                true,
                "Nuri",
                "Can",
                "ÖZTÜRK",
                LocalDate.of(1988, 8, 10),
                LocalDateTime.now(),
                LocalDate.of(1992, 7, 5)
        );

        //Given
        var resultDto = new ResponseMessage<>("User updated successfully!", 200, updatedUser);

        when(adminService.updateUser(userUpdateDTO)).thenReturn(resultDto);
        //Act
        var response = m_adminController.updateUserByUsername(userUpdateDTO);

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
        var result = (ResponseMessage<UserShowingAdminDTO>) response.getBody();
        assertEquals(result.getObject().username(), updatedUser.username());
    }

    @Test
    public void testFindAllUserCount_thenReturnTotalUserCount()
    {
        //Given
        when(adminService.findAllUserCount()).thenReturn(3L);
        //Act
        var response = m_adminController.findAllUserCount();

        //Assert
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}