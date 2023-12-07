package callofproject.dev.project.controller;

import callofproject.dev.project.service.ProjectService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/Projects")
public class AdminController
{
    private final ProjectService m_projectService;

    public AdminController(ProjectService projectService)
    {
        m_projectService = projectService;
    }


}
