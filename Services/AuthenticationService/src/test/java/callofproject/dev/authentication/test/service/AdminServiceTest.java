package callofproject.dev.authentication.test.service;

import callofproject.dev.authentication.DatabaseCleaner;
import callofproject.dev.authentication.Injection;
import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.admin.UserShowingAdminDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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

import static callofproject.dev.authentication.util.Util.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {BASE_PACKAGE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class AdminServiceTest
{
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    @Autowired
    private Injection m_injection;

    @Autowired
    private PasswordEncoder m_passwordEncoder;
    private UUID user1Id;
    private UUID user2Id;

    private UUID adminId;

    @BeforeEach
    public void setUpAndCheckUsers()
    {
        var rootUser = new User("cop_root", "root", "root", "root", "nuricanozturk02@gmail.com",
                m_passwordEncoder.encode("cop123"), LocalDate.now(), new Role(RoleEnum.ROLE_ROOT.getRole()));

        rootUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
        rootUser.addRoleToUser(new Role(RoleEnum.ROLE_ADMIN.getRole()));
        var profile1 = new UserProfile();
        profile1.setUser(rootUser);
        rootUser.setUserProfile(profile1);

        m_injection.getUserRepository().save(rootUser);


        var adminUser = new User("cop_admin", "admin", "admin", "admin", "nuricanozturk01@gmail.com",
                m_passwordEncoder.encode("cop_123"), LocalDate.now(), new Role(RoleEnum.ROLE_ADMIN.getRole()));
        adminUser.setUserProfile(new UserProfile());
        adminUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
        var profile2 = new UserProfile();
        profile2.setUser(adminUser);
        adminUser.setUserProfile(profile2);
        m_injection.getUserRepository().save(adminUser);


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

        Assertions.assertNotNull(savedUser1);
        Assertions.assertNotNull(savedUser2);

        user1Id = savedUser1.getObject().userId();
        user2Id = savedUser2.getObject().userId();
        adminId = adminUser.getUserId();
    }

    @Test
    public void testDeleteUser_withGivenUsername_shouldBeEqual()
    {
        var user = m_injection.getAdminService().removeUser("canozturk");
        Assertions.assertNotNull(user);
        assertTrue(user.getObject());
        assertEquals(200, user.getStatusCode());
    }

    @Test
    public void testFindAllUsersPageable_withGivenPageNumber_shouldReturnFour()
    {
        var response = m_injection.getAdminService().findAllUsersPageable(1);
        response.getObject().users().stream().map(UserShowingAdminDTO::username).forEach(System.out::println);
        Assertions.assertNotNull(response);
        assertEquals(4, response.getObject().users().size());
    }

    @Test
    public void testFindUsersByUsernameContainsIgnoreCase_withGivenPageNumberAndWord_ShouldSizeOne()
    {
        var response = m_injection.getAdminService().findUsersByUsernameContainsIgnoreCase(1, "Can");
        Assertions.assertNotNull(response);
        assertEquals(1, response.getObject().users().size());
    }

    @Test
    public void testFindUsersByUsernameNotContainsIgnoreCase_withGivenPageAndWord_should()
    {
        var response = m_injection.getAdminService().findUsersByUsernameNotContainsIgnoreCase(1, "c");
        Assertions.assertNotNull(response);
        response.getObject().users().stream().map(UserShowingAdminDTO::username).forEach(System.out::println);
        assertEquals(1, response.getObject().users().size());
    }

    @Test
    public void testUpdateUser_withGivenUpdateDTO_shouldBeUpdated()
    {
        String username = "halil";

        UserUpdateDTOAdmin userUpdateDTO = new UserUpdateDTOAdmin(
                adminId.toString(),
                username,
                "UpdatedFirstName",
                "UpdatedLastName",
                "UpdatedMiddleName",
                "UpdatedEmail@example.com",
                false,
                LocalDate.of(1995, 10, 15)
        );

        var response = m_injection.getAdminService().updateUser(userUpdateDTO);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getObject());
        assertEquals(username, response.getObject().username());
        assertEquals("UpdatedFirstName", response.getObject().firstName());
        assertEquals("UpdatedLastName", response.getObject().lastName());
        assertEquals("UpdatedMiddleName", response.getObject().middleName());
        assertEquals("UpdatedEmail@example.com", response.getObject().email());
        assertFalse(response.getObject().isAccountBlocked());
        assertEquals(LocalDate.of(1995, 10, 15), response.getObject().birthDate());
    }

    @Test
    public void testUpdateUser_withGivenInvalidAdminId_shouldThrowDataServiceException()
    {
        String username = "halil";

        UserUpdateDTOAdmin userUpdateDTO = new UserUpdateDTOAdmin(
                UUID.randomUUID().toString(),
                username,
                "UpdatedFirstName",
                "UpdatedLastName",
                "UpdatedMiddleName",
                "UpdatedEmail@example.com",
                false,
                LocalDate.of(1995, 10, 15)
        );

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAdminService().updateUser(userUpdateDTO));

        assertEquals("Message: AdminService::updateUser , Cause Message:Message: User not found! ",
                exception.getMessage());
    }

    @Test
    public void testUpdateUser_withGivenInvalidUsername_shouldThrowDataServiceException()
    {
        String username = "qwerty";

        UserUpdateDTOAdmin userUpdateDTO = new UserUpdateDTOAdmin(
                adminId.toString(),
                username,
                "UpdatedFirstName",
                "UpdatedLastName",
                "UpdatedMiddleName",
                "UpdatedEmail@example.com",
                false,
                LocalDate.of(1995, 10, 15)
        );

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAdminService().updateUser(userUpdateDTO));

        assertEquals("Message: AdminService::updateUser , Cause Message:Message: User not found! ",
                exception.getMessage());
    }


    @Test
    public void testUpdateUser_withGivenUnauthorizedUserId_shouldThrowDataServiceException()
    {
        String username = "halil";

        UserUpdateDTOAdmin userUpdateDTO = new UserUpdateDTOAdmin(
                user1Id.toString(),
                username,
                "UpdatedFirstName",
                "UpdatedLastName",
                "UpdatedMiddleName",
                "UpdatedEmail@example.com",
                false,
                LocalDate.of(1995, 10, 15)
        );

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAdminService().updateUser(userUpdateDTO));

        assertEquals("Message: AdminService::updateUser , Cause Message:Message: Denied Permissions! ",
                exception.getMessage());
    }


    @Test
    public void testUpdateRootUser_withGivenAdminId_shouldThrowDataServiceException()
    {
        String username = "cop_root";

        UserUpdateDTOAdmin userUpdateDTO = new UserUpdateDTOAdmin(
                adminId.toString(),
                username,
                "UpdatedFirstName",
                "UpdatedLastName",
                "UpdatedMiddleName",
                "UpdatedEmail@example.com",
                false,
                LocalDate.of(1995, 10, 15)
        );

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getAdminService().updateUser(userUpdateDTO));

        assertEquals("Message: AdminService::updateUser , Cause Message:Message: You cannot edit this user! ",
                exception.getMessage());
    }

    @Test
    public void testFindAllUserCount_shouldReturnsFour()
    {
        long count = m_injection.getAdminService().findAllUserCount();
        assertEquals(4, count);
    }

    @Test
    public void testFindNewUsersLastNday_withGivenDay_shouldReturnFour()
    {
        long newUsersCount = m_injection.getAdminService().findNewUsersLastNday(2);
        assertEquals(4, newUsersCount);
    }

    @AfterEach
    public void tearDown()
    {
        System.out.println("cleaning up");
        m_databaseCleaner.clearH2Database();
    }
}