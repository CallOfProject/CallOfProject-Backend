package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.MessageResponseDTO;
import callofproject.dev.authentication.dto.MultipleMessageResponseDTO;
import callofproject.dev.authentication.dto.UsersShowingAdminDTO;
import callofproject.dev.authentication.service.AdminService;
import callofproject.dev.authentication.service.AuthenticationService;
import callofproject.dev.authentication.service.RootService;
import callofproject.dev.authentication.service.UserManagementService;
import callofproject.dev.library.exception.service.DataServiceException;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/admin")
public class AdminController
{
    private final AuthenticationService m_authenticationService;
    private final UserManagementService m_userManagementService;
    private final RootService m_rootService;
    private final AdminService m_adminService;

    public AdminController(AuthenticationService authenticationService, UserManagementService userManagementService, RootService rootService, AdminService adminService)
    {
        m_authenticationService = authenticationService;
        m_userManagementService = userManagementService;
        m_rootService = rootService;
        m_adminService = adminService;
    }

    @GetMapping("find/all/page")
    public ResponseEntity<MultipleMessageResponseDTO<UsersShowingAdminDTO>> findAllUserByPage(@RequestParam("p") int page)
    {
        try
        {
            return ResponseEntity.ok(m_adminService.findAllUsersPageable(page));
        } catch (DataServiceException ex)
        {
            return new ResponseEntity<>(new MultipleMessageResponseDTO<>(page, 0, "", HttpStatus.SC_INTERNAL_SERVER_ERROR, null),
                    HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }

    /*public ResponseEntity<MessageResponseDTO<Boolean>> removeUser(@RequestParam("uname") String username)
    {
        return ResponseEntity.ok(m_adminService.removeUser(username));
    }

    @DeleteMapping("find/all/page")
    public ResponseEntity<MultipleMessageResponseDTO<UsersShowingAdminDTO>> findUser(@RequestParam("p") int page)
    {
        try
        {
            return ResponseEntity.ok(m_adminService.findAllUsersPageable(page));
        } catch (DataServiceException ex)
        {
            return new ResponseEntity<>(new MultipleMessageResponseDTO<>(page, 0, "", HttpStatus.SC_INTERNAL_SERVER_ERROR, null),
                    HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }*/


    @GetMapping("find/all/contains/page")
    public ResponseEntity<MultipleMessageResponseDTO<UsersShowingAdminDTO>> findUsersByUsernameContainsIgnoreCase
            (@RequestParam("p") int page, String word)
    {
        return ResponseEntity.ok(m_adminService.findUsersByUsernameContainsIgnoreCase(page, word));
    }


    @GetMapping("find/all/ignore/page")
    public ResponseEntity<MultipleMessageResponseDTO<UsersShowingAdminDTO>> findUsersByUsernameNotContainsIgnoreCase
            (@RequestParam("p") int page, String word)
    {
        return ResponseEntity.ok(m_adminService.findUsersByUsernameNotContainsIgnoreCase(page, word));
    }
}
