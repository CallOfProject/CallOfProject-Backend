package callofproject.dev.service.notification.dto;

import java.util.UUID;

public class NotificationObject
{
    public UUID projectId;
    public UUID userId;

    public NotificationObject()
    {
    }

    @Override
    public String toString()
    {
        return "NotificationObject{" +
                "projectId=" + projectId +
                ", userId=" + userId +
                '}';
    }


    public NotificationObject(UUID projectId, UUID userId)
    {
        this.projectId = projectId;
        this.userId = userId;
    }

    public void setProjectId(UUID projectId)
    {
        this.projectId = projectId;
    }

    public void setUserId(UUID userId)
    {
        this.userId = userId;
    }

    public UUID getProjectId()
    {
        return projectId;
    }

    public UUID getUserId()
    {
        return userId;
    }
}
