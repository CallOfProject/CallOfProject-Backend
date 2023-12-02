package callofproject.dev.service.notification.dto;

import callofproject.dev.nosql.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.UUID;


@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationDTO
{
    private UUID toUserId;
    private UUID fromUserId;
    private String message;
    private NotificationType notificationType;
    private Object notificationData;
    private String notificationLink;

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


    public void setToUserId(UUID toUserId)
    {
        this.toUserId = toUserId;
    }

    public void setFromUserId(UUID fromUserId)
    {
        this.fromUserId = fromUserId;
    }

    public void setMessage(String message)
    {
        this.message = message;
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
