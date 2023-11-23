package callofproject.dev.project.controller;

import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/project")
public class ProjectController
{
    private final ProjectService m_projectService;

    public ProjectController(ProjectService projectService)
    {
        m_projectService = projectService;
    }

    @PostMapping("/create")
    public ResponseEntity<Object> save(@RequestBody ProjectSaveDTO saveDTO)
    {
        return ResponseEntity.ok(m_projectService.saveProject(saveDTO));
    }


    @GetMapping("/all")
    public ResponseEntity<Object> save()
    {
        return ResponseEntity.ok(m_projectService.findAll());
    }
}
