package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.entity.enums.EProjectStatus;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;

import java.util.UUID;

/**
 * Interface for the Project Owner Service. This interface defines methods for operations
 * a project owner can perform, such as managing participants and updating project status.
 */
public interface IProjectOwnerService
{

    /**
     * Adds a participant to a project.
     *
     * @param dto Data Transfer Object containing details for saving a project participant.
     * @return A ResponseMessage containing a Boolean result of the operation.
     */
    ResponseMessage<Boolean> addParticipant(SaveProjectParticipantDTO dto);

    /**
     * Removes a participant from a project.
     *
     * @param projectId The UUID of the project.
     * @param userId    The UUID of the user to be removed.
     * @return A ResponseMessage containing the result of the operation.
     */
    ResponseMessage<Object> removeParticipant(UUID projectId, UUID userId);

    /**
     * Approves a participant's request to join a project.
     *
     * @param requestDTO Data Transfer Object containing the participant request details.
     * @return A ResponseMessage containing the result of the approval operation.
     */
    ResponseMessage<Object> approveParticipantRequest(ParticipantRequestDTO requestDTO);

    /**
     * Marks a project as finished.
     *
     * @param userId    The UUID of the user who is finishing the project.
     * @param projectId The UUID of the project to be finished.
     * @return A ResponseMessage containing the result of the finish operation.
     */
    ResponseMessage<Object> finishProject(UUID userId, UUID projectId);

    /**
     * Changes the status of a project.
     *
     * @param userId        The UUID of the user changing the project status.
     * @param projectId     The UUID of the project whose status is being changed.
     * @param projectStatus The new status of the project.
     * @return A ResponseMessage containing the result of the status change.
     */
    ResponseMessage<Object> changeProjectStatus(UUID userId, UUID projectId, EProjectStatus projectStatus);

    /**
     * Removes a project.
     *
     * @param userId    The UUID of the user removing the project.
     * @param projectId The UUID of the project to be removed.
     * @return A ResponseMessage containing the result of the remove operation.
     */
    ResponseMessage<Object> removeProject(UUID userId, UUID projectId);

    /**
     * Callback for approving a participant's request to join a project.
     *
     * @param requestDTO Data Transfer Object containing the participant request details.
     * @return A ResponseMessage containing the result of the approval operation callback.
     */
    ResponseMessage<Object> approveParticipantRequestCallback(ParticipantRequestDTO requestDTO);

    /**
     * Callback for removing a participant from a project.
     *
     * @param projectId The UUID of the project.
     * @param userId    The UUID of the user to be removed.
     * @return A ResponseMessage containing the result of the remove operation callback.
     */
    ResponseMessage<Object> removeParticipantCallback(UUID projectId, UUID userId);
}
