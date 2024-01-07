package callofproject.dev.service.notification.dto;

import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO
{
    private String notificationId;
    private UUID toUserId;
    private UUID fromUserId;
    private String message;
    private NotificationType notificationType;
    private Object notificationData;
    private String notificationLink;
    private String notificationImage;
    private String notificationTitle;
    private LocalDateTime createdAt;
    private NotificationDataType notificationDataType;
    private UUID requestId;
    private String notificationApproveLink;
    private String notificationRejectLink;

    private NotificationDTO()
    {
    }

    public static class Builder
    {
        private final NotificationDTO m_notificationDTO;

        public Builder()
        {
            m_notificationDTO = new NotificationDTO();
        }

        public Builder setNotificationId(String notificationId)
        {
            m_notificationDTO.notificationId = notificationId;
            return this;
        }
        public Builder setNotificationApproveLink(String approveLink)
        {
            m_notificationDTO.notificationApproveLink = approveLink;
            return this;
        }

        public Builder setNotificationRejectLink(String rejectLink)
        {
            m_notificationDTO.notificationRejectLink = rejectLink;
            return this;
        }

        public Builder setRequestId(UUID requestId)
        {
            m_notificationDTO.requestId = requestId;
            return this;
        }
        public Builder setNotificationDataType(NotificationDataType notificationDataType)
        {
            m_notificationDTO.notificationDataType = notificationDataType;
            return this;
        }
        public Builder setCreatedAt(LocalDateTime createdAt)
        {
            m_notificationDTO.createdAt = createdAt;
            return this;
        }

        public Builder setNotificationTitle(String notificationTitle)
        {
            m_notificationDTO.notificationTitle = notificationTitle;
            return this;
        }

        public Builder setNotificationImage(String notificationImage)
        {
            m_notificationDTO.notificationImage = notificationImage;
            return this;
        }

        public Builder setToUserId(UUID toUserId)
        {
            m_notificationDTO.toUserId = toUserId;
            return this;
        }

        public Builder setFromUserId(UUID fromUserId)
        {
            m_notificationDTO.fromUserId = fromUserId;
            return this;
        }

        public Builder setMessage(String message)
        {
            m_notificationDTO.message = message;
            return this;
        }

        public Builder setNotificationType(NotificationType notificationType)
        {
            m_notificationDTO.notificationType = notificationType;
            return this;
        }

        public Builder setNotificationData(Object notificationData)
        {
            m_notificationDTO.notificationData = notificationData;
            return this;
        }

        public Builder setNotificationLink(String notificationLink)
        {
            m_notificationDTO.notificationLink = notificationLink;
            return this;
        }

        public NotificationDTO build()
        {
            return m_notificationDTO;
        }
    }

    public String getNotificationId()
    {
        return notificationId;
    }

    public UUID getRequestId()
    {
        return requestId;
    }
    public NotificationDataType getNotificationDataType()
    {
        return notificationDataType;
    }

    public String getNotificationImage()
    {
        return notificationImage;
    }

    public String getNotificationApproveLink()
    {
        return notificationApproveLink;
    }

    public String getNotificationRejectLink()
    {
        return notificationRejectLink;
    }

    public String getNotificationTitle()
    {
        return notificationTitle;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }


    public void setToUserId(UUID toUserId)
    {
        this.toUserId = toUserId;
    }

    public void setNotificationId(String notificationId)
    {
        this.notificationId = notificationId;
    }

    public void setFromUserId(UUID fromUserId)
    {
        this.fromUserId = fromUserId;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setNotificationImage(String notificationImage)
    {
        this.notificationImage = notificationImage;
    }

    public void setNotificationTitle(String notificationTitle)
    {
        this.notificationTitle = notificationTitle;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public void setNotificationDataType(NotificationDataType notificationDataType)
    {
        this.notificationDataType = notificationDataType;
    }

    public void setRequestId(UUID requestId)
    {
        this.requestId = requestId;
    }

    public void setNotificationApproveLink(String notificationApproveLink)
    {
        this.notificationApproveLink = notificationApproveLink;
    }

    public void setNotificationRejectLink(String notificationRejectLink)
    {
        this.notificationRejectLink = notificationRejectLink;
    }

    public void setNotificationType(NotificationType notificationType)
    {
        this.notificationType = notificationType;
    }

    public void setNotificationData(Object notificationData)
    {
        this.notificationData = notificationData;
    }

    public void setNotificationLink(String notificationLink)
    {
        this.notificationLink = notificationLink;
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


    @Override
    public String toString()
    {
        return "ProjectParticipantRequestDTO{" +
                "toUserId=" + toUserId +
                ", fromUserId=" + fromUserId +
                ", message='" + message + '\'' +
                ", notificationType=" + notificationType +
                ", notificationData=" + notificationData +
                ", notificationLink='" + notificationLink + '\'' +
                '}';
    }
}
