package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.dto.ErrorMessage;
import callofproject.dev.authentication.service.RootService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/root")
@SecurityRequirement(name = "Authorization")
public class RootController
{
    private final RootService m_rootService;

    public RootController(RootService rootService)
    {
        m_rootService = rootService;
    }

    private ResponseEntity<Object> createErrorMessage(String msg, boolean status, int statusCode)
    {
        return internalServerError().body(new ErrorMessage(msg, status, statusCode));
    }


    /**
     * Give role to User.
     *
     * @param username represent the username
     * @return boolean value success or not
     */
    @PostMapping("/give/role/admin")
    public ResponseEntity<Object> giveAdminRoleToUser(String username)
    {
        return subscribe(() -> ok(m_rootService.giveAdminRoleByUsername(username)),
                msg -> createErrorMessage(msg.getMessage(), false, 500));
    }


    /**
     * Remove role from User.
     *
     * @param username represent the username
     * @return boolean value success or not
     */
    @PostMapping("/remove/role/admin")
    public ResponseEntity<Object> removeAdminRoleFromUser(String username)
    {
        return subscribe(() -> ok(m_rootService.removeAdminRoleByUsername(username)),
                msg -> createErrorMessage(msg.getMessage(), false, 500));
    }
}
