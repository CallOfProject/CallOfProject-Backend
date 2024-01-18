package callofproject.dev.project.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;

import java.util.UUID;

/**
 * Interface for the admin service. This interface defines methods for admin-related operations
 * such as cancelling projects and finding all projects with pagination.
 */
public interface IAdminService
{
    /**
     * Cancels a project given its unique identifier.
     * This method is responsible for initiating the process to cancel a project.
     *
     * @param projectId The UUID of the project to be cancelled.
     * @return A ResponseMessage containing an object, usually providing information about the operation's success or failure.
     */
    ResponseMessage<Object> cancelProject(UUID projectId);

    /**
     * Callback method for after a project cancellation is processed.
     * This method might be used for operations that need to be performed after a project has been successfully cancelled.
     *
     * @param projectId The UUID of the cancelled project.
     * @return A ResponseMessage containing an object, typically details or status of the post-cancellation process.
     */
    ResponseMessage<Object> cancelProjectCallback(UUID projectId);

    /**
     * Retrieves a paginated list of all projects.
     * This method is used for fetching projects in a paginated format, useful for admin dashboard listing or similar use cases.
     *
     * @param page The page number for which the data is to be fetched.
     * @return A MultipleResponseMessagePageable containing a list of objects (projects) with pagination information.
     */
    MultipleResponseMessagePageable<Object> findAll(int page);
}
