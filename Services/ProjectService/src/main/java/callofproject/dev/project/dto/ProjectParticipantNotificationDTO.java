package callofproject.dev.project.dto;

import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;

import java.util.UUID;

public class ProjectParticipantNotificationDTO
{
    private UUID toUserId;
    private UUID fromUserId;
    private String message;
    private NotificationType notificationType;
    private Object notificationData;
    private String notificationLink;
    private String notificationImage;
    private String notificationTitle;
    private String notificationApproveLink;
    private String notificationRejectLink;
    private NotificationDataType notificationDataType;
    private UUID requestId;

    private ProjectParticipantNotificationDTO()
    {
    }

    public static class Builder
    {
        private final ProjectParticipantNotificationDTO m_projectParticipantNotificationDTO;

        public Builder()
        {
            m_projectParticipantNotificationDTO = new ProjectParticipantNotificationDTO();
        }

        public Builder setNotificationTitle(String notificationTitle)
        {
            m_projectParticipantNotificationDTO.notificationTitle = notificationTitle;
            return this;
        }

        public Builder setNotificationDataType(NotificationDataType notificationDataType)
        {
            m_projectParticipantNotificationDTO.notificationDataType = notificationDataType;
            return this;
        }

        public Builder setRequestId(UUID requestId)
        {
            m_projectParticipantNotificationDTO.requestId = requestId;
            return this;
        }

        public Builder setNotificationImage(String notificationImage)
        {
            m_projectParticipantNotificationDTO.notificationImage = notificationImage;
            return this;
        }

        public Builder setApproveLink(String approveLink)
        {
            m_projectParticipantNotificationDTO.notificationApproveLink = approveLink;
            return this;
        }

        public Builder setRejectLink(String rejectLink)
        {
            m_projectParticipantNotificationDTO.notificationRejectLink = rejectLink;
            return this;
        }
        public Builder setToUserId(UUID toUserId)
        {
            m_projectParticipantNotificationDTO.toUserId = toUserId;
            return this;
        }

        public Builder setFromUserId(UUID fromUserId)
        {
            m_projectParticipantNotificationDTO.fromUserId = fromUserId;
            return this;
        }

        public Builder setMessage(String message)
        {
            m_projectParticipantNotificationDTO.message = message;
            return this;
        }

        public Builder setNotificationType(NotificationType notificationType)
        {
            m_projectParticipantNotificationDTO.notificationType = notificationType;
            return this;
        }

        public Builder setNotificationData(Object notificationData)
        {
            m_projectParticipantNotificationDTO.notificationData = notificationData;
            return this;
        }

        public Builder setNotificationLink(String notificationLink)
        {
            m_projectParticipantNotificationDTO.notificationLink = notificationLink;
            return this;
        }

        public ProjectParticipantNotificationDTO build()
        {
            return m_projectParticipantNotificationDTO;
        }
    }

    public NotificationDataType getNotificationDataType()
    {
        return notificationDataType;
    }

    public UUID getRequestId()
    {
        return requestId;
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

    public String getNotificationImage()
    {
        return notificationImage;
    }

    public String getNotificationTitle()
    {
        return notificationTitle;
    }

    public String getNotificationApproveLink()
    {
        return notificationApproveLink;
    }

    public String getNotificationRejectLink()
    {
        return notificationRejectLink;
    }
}
