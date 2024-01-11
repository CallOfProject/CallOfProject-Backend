package callofproject.dev.project.controller;

import callofproject.dev.data.common.clas.ErrorMessage;
import callofproject.dev.project.service.AdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.*;

@RestController
@RequestMapping("api/project/admin")
@SecurityRequirement(name = "Authorization")
public class AdminController
{
    private final AdminService m_adminService;

    public AdminController(AdminService adminService)
    {
        m_adminService = adminService;
    }

    @PostMapping("/cancel")
    public ResponseEntity<Object> cancelProject(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_adminService.cancelProject(projectId)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Find all project
     *
     * @param page is page number
     * @return if success ProjectDTO else return Error Message
     */
    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_adminService.findAll(page)), msg -> badRequest().body(msg.getMessage()));
    }

}
