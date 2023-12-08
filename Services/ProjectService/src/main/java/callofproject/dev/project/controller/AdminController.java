package callofproject.dev.project.controller;

import callofproject.dev.data.common.clas.ErrorMessage;
import callofproject.dev.project.service.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/admin/project")
public class AdminController
{
    private final AdminService m_adminService;

    public AdminController(AdminService adminService)
    {
        m_adminService = adminService;
    }

    @PostMapping("cancel")
    public ResponseEntity<Object> cancelProject(UUID projectId)
    {
        return subscribe(() -> ok(m_adminService.cancelProject(projectId)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

}
