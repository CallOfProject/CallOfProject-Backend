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
public class RootServiceTest
{
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    @Autowired
    private Injection m_injection;

    @Autowired
    private PasswordEncoder m_passwordEncoder;

    private UUID user1Id;
    private UUID user2Id;
    private UUID rootId;
    private UUID adminId;

    @BeforeEach
    public void setUpAndCheckUsers()
    {
        /*var rootUser = new User("cop_root", "root", "root", "root", "nuricanozturk02@gmail.com",
                m_passwordEncoder.encode("cop123"), LocalDate.now(), new Role(RoleEnum.ROLE_ROOT.getRole()));

        rootUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
        rootUser.addRoleToUser(new Role(RoleEnum.ROLE_ADMIN.getRole()));
        var profile1 = new UserProfile();
        profile1.setUser(rootUser);
        rootUser.setUserProfile(profile1);

        m_injection.getUserRepository().save(rootUser);*/

/*
        var adminUser = new User("cop_admin", "admin", "admin", "admin", "nuricanozturk01@gmail.com",
                m_passwordEncoder.encode("cop_123"), LocalDate.now(), new Role(RoleEnum.ROLE_ADMIN.getRole()));
        adminUser.setUserProfile(new UserProfile());
        adminUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
        var profile2 = new UserProfile();
        profile2.setUser(adminUser);
        adminUser.setUserProfile(profile2);
        m_injection.getUserRepository().save(adminUser);*/


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
        //adminId = adminUser.getUserId();
        // rootId = rootUser.getUserId();
    }

    @Test
    public void testGiveAdminRoleToUser_withGivenUserId_shouldNotNull()
    {
        var user1 = m_injection.getUserManagementService().getUserIfExists(user1Id);
        var user2 = m_injection.getUserManagementService().getUserIfExists(user2Id);

        assertNotNull(user1);
        assertNotNull(user2);

        var user1WithAdminRole = m_injection.getRootService()
                .giveAdminRoleByUsername(user1.getUsername());
        var user2WithAdminRole = m_injection.getRootService().giveAdminRoleByUsername(user2.getUsername());

        assertNotNull(user1WithAdminRole);
        assertNotNull(user2WithAdminRole);

        assertTrue(user1WithAdminRole.getObject());
        assertTrue(user2WithAdminRole.getObject());
    }


    @Test
    public void testGiveAdminRoleToUser_withGivenUserId_shouldThrowDataServiceException()
    {
        var user1 = m_injection.getUserManagementService().getUserIfExists(user1Id);
        assertNotNull(user1);

        // Give admin role to user
        var user1WithAdminRole = m_injection.getRootService().giveAdminRoleByUsername(user1.getUsername());
        assertNotNull(user1WithAdminRole);
        assertTrue(user1WithAdminRole.getObject());

        // Try to give admin role to user again
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getRootService().giveAdminRoleByUsername(user1.getUsername()));
        assertEquals("Message: RootService::giveAdminRoleByUsername , Cause Message:Message: User already has admin role! ",
                exception.getMessage());
    }


    @Test
    public void testGiveAdminRoleToNonExistingUser_withGivenUserId_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getRootService().giveAdminRoleByUsername("non_existing_user"));

        assertEquals("Message: RootService::giveAdminRoleByUsername , Cause Message:Message: User does not exists! ",
                exception.getMessage());
    }


    @AfterEach
    public void tearDown()
    {
        System.out.println("cleaning up");
        m_databaseCleaner.clearH2Database();
    }
}
