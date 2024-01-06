package callofproject.dev.project.controller;

import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import callofproject.dev.project.service.ProjectService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/project")
@SecurityRequirement(name = "Authorization")
public class ProjectController
{
    private final ProjectService m_projectService;

    public ProjectController(ProjectService projectService)
    {
        m_projectService = projectService;
    }

    /**
     * Save project
     * Projeyi kaydeder.
     *
     * @param saveDTO is projectSaveDTO
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/create")
    public ResponseEntity<Object> save(@RequestBody @Valid ProjectSaveDTO saveDTO)
    {
        return subscribe(() -> ok(m_projectService.saveProject(saveDTO)), msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Update project
     * Projeyi günceller.
     *
     * @param dto is projectUpdateDTO
     * @return if success ProjectDTO else return Error Message
     */
    @PutMapping("/update")
    public ResponseEntity<Object> updateProject(@RequestBody ProjectUpdateDTO dto)
    {
        return subscribe(() -> ok(m_projectService.updateProject(dto)), msg -> internalServerError().body(msg.getMessage()));
    }


    /**
     * Find all project by user id who is participant in project.
     * Kullanıcının katılımcı olduğu projeleri sayfa sayfa getirir.
     *
     * @param userId is user id
     * @param page   is page number
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/participant/user-id")
    public ResponseEntity<Object> findAllParticipantProjectByUserId(@RequestParam("uid") String userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllParticipantProjectByUserId(UUID.fromString(userId), page)),
                msg -> internalServerError().body(msg.getMessage()));
    }


    /**
     * Find all owner of project by given user id and page
     * Kullanıcının sahip olduğu projeleri sayfa sayfa getirir.
     *
     * @param userId is user id
     * @param page   is page number
     * @return if success ProjectDTO else return Error Message
     */
    @GetMapping("find/all/owner-id")
    public ResponseEntity<Object> findAllOwnerProjectsByUserId(@RequestParam("uid") UUID userId, @RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllOwnerProjectsByUserId(userId, page)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Find all participant of project by given username and page
     * Kullanıcının sahip olduğu projeleri sayfa sayfa getirir.
     *
     * @param username is username
     * @param page     is page number
     * @return if success ProjectDTO else return Error Message
     */
    @GetMapping("find/all/owner-username")
    public ResponseEntity<Object> findAllOwnerProjectsByUsername(String username, int page)
    {
        return subscribe(() -> ok(m_projectService.findAllOwnerProjectsByUsername(username, page)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Find project Overview by project id
     * Projenin genel bilgilerini getirir.
     *
     * @param projectId is project id
     * @return if success ProjectDTO else return Error Message
     */
    @GetMapping("find/overview")
    public ResponseEntity<Object> findProjectOverview(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectService.findProjectOverview(projectId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Find project by view of owner
     * Projenin sahibi tarafından görüntülenmesini sağlar.
     *
     * @param userId    is user id
     * @param projectId is project id
     * @return if success ProjectDetailDTO else return Error Message
     */
    @GetMapping("find/detail/owner")
    public ResponseEntity<Object> findProjectOwnerView(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectService.findProjectOwnerView(userId, projectId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Find projects by view of discovery
     * Projelerin keşif görünümünü getirir.
     *
     * @return if success ProjectsDiscoveryDTO else return Error Message
     */
    @GetMapping("discovery/all")
    public ResponseEntity<Object> findAllProjectDiscoveryView(@RequestParam("p") int page)
    {
        return subscribe(() -> ok(m_projectService.findAllProjectDiscoveryView(page)), msg -> internalServerError().body(msg.getMessage()));
    }

    /**
     * Send Project Participant Request
     *
     * @param projectId is project id
     * @param userId    is user id
     * @return if success ProjectDTO else return Error Message
     */
    @PostMapping("/participant/request")
    public ResponseEntity<Object> addProjectJoinRequest(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_projectService.addProjectJoinRequest(projectId, userId)),
                msg -> internalServerError().body(msg.getMessage()));
    }

    @GetMapping("/find/detail")
    public ResponseEntity<Object> findProjectDetail(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectService.findProjectDetail(projectId)),
                msg -> internalServerError().body(msg.getMessage()));
    }
}