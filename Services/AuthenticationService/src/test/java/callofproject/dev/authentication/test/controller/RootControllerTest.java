package callofproject.dev.authentication.test.controller;

import callofproject.dev.authentication.controller.RootController;
import callofproject.dev.authentication.dto.admin.UsersShowingAdminDTO;
import callofproject.dev.authentication.service.RootService;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RootControllerTest
{
    @Mock
    private RootService m_rootService;
    @InjectMocks
    private RootController m_rootController;

    private UsersShowingAdminDTO m_usersShowingAdminDTO;

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
                "emirkafadar",
                "Emir",
                "",
                "KAFADAR",
                "emir@example.com",
                "12345",
                LocalDate.of(1990, 5, 15),
                new Role("ROLE_ADMIN")
        );
    }


    @Test
    public void testGiveAdminRoleToUser_withGivenValidUsername_shouldNotNull()
    {
        var expectedResult = new ResponseMessage<Boolean>("Admin role given to user successfully!", 200, true);
        when(m_rootService.giveAdminRoleByUsername(any())).thenReturn(expectedResult);
        var response = m_rootController.giveAdminRoleToUser("ahmetkoc");
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(expectedResult.getObject());
    }

    @Test
    public void testRemoveAdminRoleFromUser_withGivenValidUsername_shouldNotNull()
    {
        var expectedResult = new ResponseMessage<Boolean>("Admin role removed from user successfully!", 200, true);
        when(m_rootService.removeAdminRoleByUsername(any())).thenReturn(expectedResult);
        var response = m_rootController.removeAdminRoleFromUser(user2.getUsername());
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertTrue(expectedResult.getObject());
    }
}