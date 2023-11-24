package callofproject.dev.project.controller;

import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.*;
import static org.springframework.http.ResponseEntity.badRequest;

@RestController
@RequestMapping("api/project")
public class ProjectController
{
    private final ProjectService m_projectService;

    public ProjectController(ProjectService projectService)
    {
        m_projectService = projectService;
    }

    /**
     * Save project
     *
     * @param saveDTO is projectSaveDTO
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/create")
    public ResponseEntity<Object> save(@RequestBody ProjectSaveDTO saveDTO)
    {
        return subscribe(() -> ok(m_projectService.saveProject(saveDTO)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Find all project
     *
     * @return if success ProjectDTO else return Error Message
     */
    @GetMapping("/all")
    public ResponseEntity<Object> findAll()
    {
        return subscribe(() -> ok(ok(m_projectService.findAll())), msg -> badRequest().body(msg.getMessage()));
    }

    @PostMapping("participant/add")
    public ResponseEntity<Object> addParticipant(@RequestParam("pid") String projectId, @RequestParam("uid") String userId)
    {
        return subscribe(() -> ok(m_projectService.addParticipant(projectId, userId)), msg -> badRequest().body(msg.getMessage()));
    }

}
