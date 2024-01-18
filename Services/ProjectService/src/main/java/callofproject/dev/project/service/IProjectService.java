package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * Interface for the Project Service. This interface defines methods for managing projects,
 * including creation, updating, and retrieval of project-related data.
 */
public interface IProjectService
{

    /**
     * Saves a new project using a ProjectSaveDTO.
     *
     * @param projectDTO Data Transfer Object containing project information.
     * @return A ResponseMessage containing the result of the save operation.
     */
    ResponseMessage<Object> saveProject(ProjectSaveDTO projectDTO);

    /**
     * Saves a new project with an additional file using ProjectSaveDTO and MultipartFile.
     *
     * @param saveDTO Data Transfer Object containing project information.
     * @param file    The file to be associated with the project.
     * @return A ResponseMessage containing the result of the save operation.
     */
    ResponseMessage<Object> saveProjectV2(ProjectSaveDTO saveDTO, MultipartFile file);

    /**
     * Retrieves all projects a user is participating in, with pagination.
     *
     * @param userId The UUID of the user.
     * @param page   The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of projects.
     */
    MultipleResponseMessagePageable<Object> findAllParticipantProjectByUserId(UUID userId, int page);

    /**
     * Updates a project using ProjectUpdateDTO.
     *
     * @param projectDTO Data Transfer Object containing updated project information.
     * @return A ResponseMessage containing the result of the update operation.
     */
    ResponseMessage<Object> updateProject(ProjectUpdateDTO projectDTO);

    /**
     * Updates a project with an additional file using ProjectUpdateDTO and MultipartFile.
     *
     * @param projectDTO Data Transfer Object containing updated project information.
     * @param file       The file to be associated with the project update.
     * @return A ResponseMessage containing the result of the update operation.
     */
    ResponseMessage<Object> updateProjectV2(ProjectUpdateDTO projectDTO, MultipartFile file);

    /**
     * Retrieves all projects owned by a user, with pagination.
     *
     * @param userId The UUID of the user.
     * @param page   The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of owned projects.
     */
    MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUserId(UUID userId, int page);

    /**
     * Retrieves all projects owned by a user, identified by username, with pagination.
     *
     * @param username The username of the project owner.
     * @param page     The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of owned projects.
     */
    MultipleResponseMessagePageable<Object> findAllOwnerProjectsByUsername(String username, int page);

    /**
     * Retrieves a view of a project from the perspective of its owner.
     *
     * @param userId    The UUID of the owner.
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectOwnerView.
     */
    ResponseMessage<Object> findProjectOwnerView(UUID userId, UUID projectId);

    /**
     * Retrieves a paginated list of projects for discovery purposes.
     *
     * @param page The page number for pagination.
     * @return A MultipleResponseMessagePageable containing a list of project discovery views.
     */
    MultipleResponseMessagePageable<Object> findAllProjectDiscoveryView(int page);

    /**
     * Retrieves an overview of a specific project.
     *
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectOverview.
     */
    ResponseMessage<Object> findProjectOverview(UUID projectId);

    /**
     * Adds a request to join a project.
     *
     * @param projectId The UUID of the project to join.
     * @param userId    The UUID of the user requesting to join.
     * @return A ResponseMessage containing the result of the join request.
     */
    ResponseMessage<Object> addProjectJoinRequest(UUID projectId, UUID userId);

    /**
     * Retrieves detailed information of a specific project.
     *
     * @param projectId The UUID of the project.
     * @return A ResponseMessage containing the ProjectDetail.
     */
    ResponseMessage<Object> findProjectDetail(UUID projectId);

    /**
     * Retrieves detailed information of a specific project if the user has permission.
     *
     * @param projectId The UUID of the project.
     * @param userId    The UUID of the user.
     * @return A ResponseMessage containing the ProjectDetail.
     */
    ResponseMessage<Object> findProjectDetailIfHasPermission(UUID projectId, UUID userId);

    /**
     * Callback for adding a request to join a project.
     *
     * @param projectId The UUID of the project to join.
     * @param userId    The UUID of the user requesting to join.
     * @return A ResponseMessage containing the result of the join request callback.
     */
    ResponseMessage<Object> addProjectJoinRequestCallback(UUID projectId, UUID userId);
}
