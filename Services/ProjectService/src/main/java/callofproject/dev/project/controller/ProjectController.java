package callofproject.dev.project.controller;

import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

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
    public ResponseEntity<Object> save(@RequestBody @Valid ProjectSaveDTO saveDTO)
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
        return subscribe(() -> ok(m_projectService.findAll()), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Find project by id
     *
     * @param dto is projectSaveDTO
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("participant/add")
    public ResponseEntity<Object> addParticipant(@RequestBody SaveProjectParticipantDTO dto)
    {
        return subscribe(() -> ok(m_projectService.addParticipant(dto)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Find all project
     *
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/participant/user-id")
    public ResponseEntity<Object> findAllParticipantProjectByUserId(@RequestParam("uid") String userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllParticipantProjectByUserId(UUID.fromString(userId), page)),
                msg -> badRequest().body(msg.getMessage()));
    }

    @PostMapping("/participant/request")
    public ResponseEntity<Object> addProjectJoinRequest(@RequestParam("pid") String projectId, @RequestParam("uid") String userId)
    {
        return subscribe(() -> ok(m_projectService.addProjectJoinRequest(projectId, userId)),
                msg -> badRequest().body(msg.getMessage()));
    }

    @PostMapping("/participant/request/approve")
    public ResponseEntity<Object> approveProjectParticipantRequest(@RequestBody ParticipantRequestDTO dto)
    {
        return subscribe(() -> ok(m_projectService.approveParticipantRequest(dto)),
                msg -> badRequest().body(msg.getMessage()));
    }
}