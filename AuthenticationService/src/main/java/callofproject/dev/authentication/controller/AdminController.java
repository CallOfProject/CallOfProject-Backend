package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.ErrorMessage;
import callofproject.dev.authentication.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
     * Find all users page by page
     *
     * @param page is page
     * @return UserShowingAdminDTO
     */
    @GetMapping("find/all/page")
    public ResponseEntity<Object> findAllUserByPage(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_adminService.findAllUsersPageable(page)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
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
                msg -> internalServerError().body(new ErrorMessage("Users Not Found!", false, 500)));
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
}
