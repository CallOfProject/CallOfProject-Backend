package callofproject.dev.project.controller;

import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.service.ProjectOwnerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/project-owner")
@SecurityRequirement(name = "Authorization")
public class ProjectOwnerController
{
    private final ProjectOwnerService m_projectOwnerService;

    public ProjectOwnerController(ProjectOwnerService projectOwnerService)
    {
        m_projectOwnerService = projectOwnerService;
    }

    /**
     * set project status to finish
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
     * Change project status
     *
     * @param userId    is user id
     * @param projectId is project id
     * @param status    is project status
     * @return if success ProjectDetailDTO else return Error Message
     */
    @PostMapping("change/status")
    public ResponseEntity<Object> changeProjectStatus(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId, @RequestParam("status") EProjectStatus status)
    {
        return subscribe(() -> ok(m_projectOwnerService.changeProjectStatus(userId, projectId, status)), msg -> badRequest().body(msg.getMessage()));
    }

    @DeleteMapping("remove")
    public ResponseEntity<Object> removeProject(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectOwnerService.removeProject(userId, projectId)), msg -> badRequest().body(msg.getMessage()));
    }

    //---------------------------------------------------PARTICIPANT----------------------------------------------------

    /**
     * Remove participant from project
     *
     * @param userId    is user id
     * @param projectId is project id
     * @return if success ProjectDetailDTO else return Error Message
     */
    @DeleteMapping("participant/remove")
    public ResponseEntity<Object> removeParticipant(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_projectOwnerService.removeParticipant(projectId, userId)), msg -> badRequest().body(msg.getMessage()));
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
}
