package callofproject.dev.project.controller;

import callofproject.dev.data.common.clas.ErrorMessage;
import callofproject.dev.project.service.IAdminService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.*;

/**
 * This class represents a controller for administrative operations related to projects.
 * It handles HTTP requests for project administration and interacts with the AdminService.
 */
@RestController
@RequestMapping("api/project/admin")
@SecurityRequirement(name = "Authorization")
public class AdminController
{
    private final IAdminService m_adminService;


    /**
     * Constructs a new AdminController with the provided AdminService dependency.
     *
     * @param adminService The AdminService instance used for handling administrative operations.
     */
    public AdminController(IAdminService adminService)
    {
        m_adminService = adminService;
    }

    /**
     * Handles the HTTP POST request to cancel a project.
     *
     * @param projectId The unique identifier (UUID) of the project to be canceled.
     * @return ResponseEntity with the result of the cancel operation, or an error message in case of failure.
     */
    @PostMapping("/cancel")
    public ResponseEntity<Object> cancelProject(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_adminService.cancelProject(projectId)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Handles the HTTP GET request to retrieve all projects.
     *
     * @param page The page number for paginated results.
     * @return ResponseEntity containing ProjectDTO if successful, or an error message if there's an issue.
     */
    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_adminService.findAll(page)), msg -> badRequest().body(msg.getMessage()));
    }

}
