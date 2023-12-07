package callofproject.dev.project.controller;

import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
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
     * Update project
     *
     * @param dto is projectUpdateDTO
     * @return if success ProjectDTO else return Error Message
     */
    @PutMapping("/update")
    public ResponseEntity<Object> updateProject(@RequestBody ProjectUpdateDTO dto)
    {
        return subscribe(() -> ok(m_projectService.updateProject(dto)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Find all project
     *
     * @return if success ProjectDTO else return Error Message
     */
    @GetMapping("/all")
    public ResponseEntity<Object> findAll(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAll(page)), msg -> badRequest().body(msg.getMessage()));
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


    /**
     * Send Project Participant Request
     *
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/participant/request")
    public ResponseEntity<Object> addProjectJoinRequest(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_projectService.addProjectJoinRequest(projectId, userId)),
                msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Approve or Reject Project Participant Request
     *
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/participant/request/approve")
    public ResponseEntity<Object> approveProjectParticipantRequest(@RequestBody ParticipantRequestDTO dto)
    {
        return subscribe(() -> ok(m_projectService.approveParticipantRequest(dto)),
                msg -> badRequest().body(msg.getMessage()));
    }

    @GetMapping("find/all/owner-id")
    public ResponseEntity<Object> findAllOwnerProjectsByUserId(@RequestParam("uid") UUID userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllOwnerProjectsByUserId(userId, page)),
                msg -> badRequest().body(msg.getMessage()));
    }

    @GetMapping("find/all/owner-username")
    public ResponseEntity<Object> findAllParticipantProjectsByUserId(String username, int page)
    {
        return subscribe(() -> ok(m_projectService.findAllOwnerProjectsByUsername(username, page)),
                msg -> badRequest().body(msg.getMessage()));
    }
}