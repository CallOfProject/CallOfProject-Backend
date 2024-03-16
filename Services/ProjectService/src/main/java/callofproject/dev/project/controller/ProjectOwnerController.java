package callofproject.dev.project.controller;

import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.service.IProjectOwnerService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.badRequest;
import static org.springframework.http.ResponseEntity.ok;

/**
 * This class represents a controller for managing project owner-related operations.
 * It handles HTTP requests related to project owners and interacts with the ProjectOwnerService.
 */
@RestController
@RequestMapping("api/project/project-owner")
@SecurityRequirement(name = "Authorization")
public class ProjectOwnerController
{
    private final IProjectOwnerService m_projectOwnerService;

    /**
     * Constructs a new ProjectOwnerController with the provided dependencies.
     *
     * @param projectOwnerService The IProjectOwnerService instance used for handling project owner-related operations.
     */
    public ProjectOwnerController(IProjectOwnerService projectOwnerService)
    {
        m_projectOwnerService = projectOwnerService;
    }

    /**
     * Handles the HTTP POST request to set the project status to "finish."
     *
     * @param userId    The unique identifier (UUID) of the user initiating the action.
     * @param projectId The unique identifier (UUID) of the project to be marked as finished.
     * @return ResponseEntity with the result of the operation as ProjectDetailDTO if successful, or an error message in case of failure.
     */
    @PostMapping("finish")
    public ResponseEntity<Object> finishProject(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectOwnerService.finishProject(userId, projectId)),
                msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to change the project status.
     *
     * @param userId    The unique identifier (UUID) of the user initiating the action.
     * @param projectId The unique identifier (UUID) of the project to change the status of.
     * @param status    The new project status.
     * @return ResponseEntity with the result of the operation as ProjectDetailDTO if successful, or an error message in case of failure.
     */
    @PostMapping("change/status")
    public ResponseEntity<Object> changeProjectStatus(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId, @RequestParam("status") EProjectStatus status)
    {
        return subscribe(() -> ok(m_projectOwnerService.changeProjectStatus(userId, projectId, status)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP DELETE request to remove a project.
     *
     * @param userId    The unique identifier (UUID) of the user initiating the action.
     * @param projectId The unique identifier (UUID) of the project to be removed.
     * @return ResponseEntity with the result of the operation as ProjectDetailDTO if successful, or an error message in case of failure.
     */
    @DeleteMapping("remove")
    public ResponseEntity<Object> removeProject(@RequestParam("uid") UUID userId, @RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectOwnerService.removeProject(userId, projectId)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP DELETE request to remove a project.
     *
     * @param projectId The unique identifier (UUID) of the project to be removed.
     * @return ResponseEntity with the result of the operation as ProjectDetailDTO if successful, or an error message in case of failure.
     */
    @DeleteMapping("delete/soft")
    public ResponseEntity<Object> softDeleteProject(@RequestParam("pid") UUID projectId)
    {
        return subscribe(() -> ok(m_projectOwnerService.softDeleteProject(projectId)), msg -> badRequest().body(msg.getMessage()));
    }

    //---------------------------------------------------PARTICIPANT----------------------------------------------------

    /**
     * Handles the HTTP DELETE request to remove a participant from a project.
     *
     * @param projectId The unique identifier (UUID) of the project from which the participant is to be removed.
     * @param userId    The unique identifier (UUID) of the participant to be removed.
     * @return ResponseEntity with the result of the operation as ProjectDetailDTO if successful, or an error message in case of failure.
     */
    @DeleteMapping("participant/remove")
    public ResponseEntity<Object> removeParticipant(@RequestParam("pid") UUID projectId, @RequestParam("uid") UUID userId)
    {
        return subscribe(() -> ok(m_projectOwnerService.removeParticipant(projectId, userId)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to add a participant to a project.
     *
     * @param dto The SaveProjectParticipantDTO containing details of the participant to be added.
     * @return ResponseEntity with the result of the operation as ProjectDTO if successful, or an error message in case of failure.
     */
    @PostMapping("participant/add")
    public ResponseEntity<Object> addParticipant(@RequestBody SaveProjectParticipantDTO dto)
    {
        return subscribe(() -> ok(m_projectOwnerService.addParticipant(dto)), msg -> badRequest().body(msg.getMessage()));
    }

    /**
     * Handles the HTTP POST request to approve or reject a project participant request.
     *
     * @param dto The ParticipantRequestDTO containing details of the request to be approved or rejected.
     * @return ResponseEntity with the result of the operation as ProjectDTO if successful, or an error message in case of failure.
     */
    @PostMapping("/participant/request/approve")
    public ResponseEntity<Object> approveProjectParticipantRequest(@RequestBody ParticipantRequestDTO dto)
    {
        return subscribe(() -> ok(m_projectOwnerService.approveParticipantRequest(dto)),
                msg -> badRequest().body(msg.getMessage()));
    }
}
