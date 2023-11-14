package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.ErrorMessage;
import callofproject.dev.authentication.dto.MultipleMessageResponseDTO;
import callofproject.dev.authentication.dto.admin.UserUpdateDTOAdmin;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/admin")
@SecurityRequirement(name = "Authorization")
public class AdminController
{
    private final AdminService m_adminService;

    public AdminController(AdminService adminService)
    {
        m_adminService = adminService;
    }

    /**
     * Login operation for admins.
     *
     * @param request represent the login information.
     * @return if success returns AuthenticationResponse that include token and status else return ErrorMessage.
     */
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request)
    {
        return subscribe(() -> ok(m_adminService.authenticate(request)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 401)));
    }

    /**
     * Find all users page by page
     *
     * @param page is page
     * @return UserShowingAdminDTO
     */
    @GetMapping("find/all/page")
    public ResponseEntity<Object> findAllUserByPage(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_adminService.findAllUsersPageable(page)), msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find all users with given parameters are word and page
     *
     * @param page is page
     * @param word contains word
     * @return Users Showing Admin DTO
     */
    @GetMapping("find/all/contains/page")
    public ResponseEntity<Object> findUsersByUsernameContainsIgnoreCase(@RequestParam("p") int page, String word)
    {
        return subscribe(() -> ok(m_adminService.findUsersByUsernameContainsIgnoreCase(page, word)),
                msg -> internalServerError().body(new MultipleMessageResponseDTO<>(0, 0, 0, "", null)));
    }

    /**
     * Find all users with given parameters are word and page
     *
     * @param page is page
     * @param word not contains word
     * @return Users Showing Admin DTO
     */
    @GetMapping("find/all/ignore/page")
    public ResponseEntity<Object> findUsersByUsernameNotContainsIgnoreCase(@RequestParam("p") int page, String word)
    {
        return subscribe(() -> ok(m_adminService.findUsersByUsernameNotContainsIgnoreCase(page, word)),
                msg -> internalServerError().body(new ErrorMessage("Users Not Found!", false, 500)));
    }

    /**
     * remove user with given username parameter
     *
     * @param username represent the user
     * @return success or not
     */
    @DeleteMapping("remove/user")
    public ResponseEntity<Object> removeUserByUsername(@RequestParam("uname") String username)
    {
        return subscribe(() -> ok(m_adminService.removeUser(username)),
                msg -> internalServerError().body(new ErrorMessage("Users Not Found!", false, 500)));
    }

    /**
     * Update user with given information.
     *
     * @param userUpdateDTO represent the user information.
     * @return the UserShowingAdminDTO.
     */
    @PutMapping("update/user")
    public ResponseEntity<Object> updateUserByUsername(@RequestBody UserUpdateDTOAdmin userUpdateDTO)
    {
        return subscribe(() -> ok(m_adminService.updateUser(userUpdateDTO)),
                msg -> internalServerError().body(new ErrorMessage("Users Not Found!", false, 500)));
    }
}
