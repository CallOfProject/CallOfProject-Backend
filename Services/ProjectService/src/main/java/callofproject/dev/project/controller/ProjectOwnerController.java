package callofproject.dev.project.controller;

import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.service.ProjectOwnerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/project-owner/project")
public class ProjectOwnerController
{
    private final ProjectOwnerService m_projectOwnerService;

    public ProjectOwnerController(ProjectOwnerService projectOwnerService)
    {
        m_projectOwnerService = projectOwnerService;
    }

    /**
     * Find all participant of project by given user id and page
     *
     * @param userId    is user id
     * @param projectId is project id
     * @return if success ProjectDetailDTO else return Error Message
     */
    @PostMapping("finish")
    public ResponseEntity<Object> finishProject(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectOwnerService.finishProject(userId, projectId)),
                msg -> badRequest().body(msg.getMessage()));
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
        return subscribe(() -> ok(m_projectOwnerService.addProjectJoinRequest(projectId, userId)),
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
        return subscribe(() -> ok(m_projectOwnerService.approveParticipantRequest(dto)),
                msg -> badRequest().body(msg.getMessage()));
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
        return subscribe(() -> ok(m_projectOwnerService.addParticipant(dto)), msg -> badRequest().body(msg.getMessage()));
    }
}
