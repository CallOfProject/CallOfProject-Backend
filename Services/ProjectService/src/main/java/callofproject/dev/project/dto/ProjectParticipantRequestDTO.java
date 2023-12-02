package callofproject.dev.project.dto;

import callofproject.dev.nosql.enums.NotificationType;

import java.util.UUID;

public class ProjectParticipantRequestDTO
{
    private UUID toUserId;
    private UUID fromUserId;
    private String message;
    private NotificationType notificationType;
    private Object notificationData;
    private String notificationLink;

    private ProjectParticipantRequestDTO()
    {
    }

    public static class Builder
    {
        private final ProjectParticipantRequestDTO m_projectParticipantRequestDTO;

        public Builder()
        {
            m_projectParticipantRequestDTO = new ProjectParticipantRequestDTO();
        }

        public Builder setToUserId(UUID toUserId)
        {
            m_projectParticipantRequestDTO.toUserId = toUserId;
            return this;
        }

        public Builder setFromUserId(UUID fromUserId)
        {
            m_projectParticipantRequestDTO.fromUserId = fromUserId;
            return this;
        }

        public Builder setMessage(String message)
        {
            m_projectParticipantRequestDTO.message = message;
            return this;
        }

        public Builder setNotificationType(NotificationType notificationType)
        {
            m_projectParticipantRequestDTO.notificationType = notificationType;
            return this;
        }

        public Builder setNotificationData(Object notificationData)
        {
            m_projectParticipantRequestDTO.notificationData = notificationData;
            return this;
        }

        public Builder setNotificationLink(String notificationLink)
        {
            m_projectParticipantRequestDTO.notificationLink = notificationLink;
            return this;
        }

        public ProjectParticipantRequestDTO build()
        {
            return m_projectParticipantRequestDTO;
        }
    }


    public UUID getToUserId()
    {
        return toUserId;
    }

    public UUID getFromUserId()
    {
        return fromUserId;
    }

    public String getMessage()
    {
        return message;
    }

    public NotificationType getNotificationType()
    {
        return notificationType;
    }

    public Object getNotificationData()
    {
        return notificationData;
    }

    public String getNotificationLink()
    {
        return notificationLink;
    }
}
