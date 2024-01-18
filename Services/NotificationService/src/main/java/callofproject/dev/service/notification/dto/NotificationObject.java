package callofproject.dev.service.notification.dto;

import java.util.UUID;

/**
 * DTO class for notification object.
 */
public class NotificationObject
{
    public UUID projectId;
    public UUID userId;

    /**
     * Constructor.
     */
    public NotificationObject()
    {
    }


    /**
     * Constructor.
     *
     * @param projectId represent the project id.
     * @param userId    represent the user id.
     */
    public NotificationObject(UUID projectId, UUID userId)
    {
        this.projectId = projectId;
        this.userId = userId;
    }


    /**
     * Set project id.
     *
     * @param projectId represent the project id.
     */
    public void setProjectId(UUID projectId)
    {
        this.projectId = projectId;
    }


    /**
     * Set user id.
     *
     * @param userId represent the user id.
     */
    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }


    /**
     * Get project id.
     *
     * @return UUID.
     */
    public UUID getProjectId()
    {
        return projectId;
    }


    /**
     * Get user id.
     *
     * @return UUID.
     */
    public UUID getUserId()
    {
        return userId;
    }
}
