package callofproject.dev.nosql.entity;

import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("notification")
@SuppressWarnings("all")
public class Notification
{
    @Id
    private String id;
    private String className;
    private UUID notificationOwnerId;
    private UUID fromUserId;
    private String message;
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;
    private Object notificationData;
    private String notificationLink;
    private String notificationApproveLink;
    private String notificationRejectLink;
    private String notificationImage;
    private String notificationTitle;
    private LocalDateTime createdAt;
    private NotificationDataType notificationDataType;

    private UUID requestId;

    public Notification()
    {
    }

    public static class Builder
    {
        private final Notification m_notification;

        public Builder()
        {
            m_notification = new Notification();
            m_notification.createdAt = LocalDateTime.now();
        }

        public Builder setRequestId(UUID requestId)
        {
            m_notification.requestId = requestId;
            return this;
        }

        public Builder setNotificationDataType(NotificationDataType notificationDataType)
        {
            m_notification.notificationDataType = notificationDataType;
            return this;
        }

        public Builder setNotificationTitle(String notificationTitle)
        {
            m_notification.notificationTitle = notificationTitle;
            return this;
        }

        public Builder setNotificationImage(String notificationImage)
        {
            m_notification.notificationImage = notificationImage;
            return this;
        }

        public Builder setNotificationApproveLink(String notificationApproveLink)
        {
            m_notification.notificationApproveLink = notificationApproveLink;
            return this;
        }

        public Builder setNotificationRejectLink(String notificationRejectLink)
        {
            m_notification.notificationRejectLink = notificationRejectLink;
            return this;
        }

        public Builder setClassName(String className)
        {
            m_notification.className = className;
            return this;
        }

        public Builder setNotificationOwnerId(UUID notificationOwnerId)
        {
            m_notification.notificationOwnerId = notificationOwnerId;
            return this;
        }

        public Builder setFromUserId(UUID fromUserId)
        {
            m_notification.fromUserId = fromUserId;
            return this;
        }

        public Builder setMessage(String message)
        {
            m_notification.message = message;
            return this;
        }

        public Builder setNotificationType(NotificationType notificationType)
        {
            m_notification.notificationType = notificationType;
            return this;
        }


        public Builder setNotificationData(Object notificationData)
        {
            m_notification.notificationData = notificationData;
            return this;
        }

        public Builder setNotificationLink(String notificationLink)
        {
            m_notification.notificationLink = notificationLink;
            return this;
        }

        public Notification build()
        {
            return m_notification;
        }
    }

    public Notification build()
    {
        Notification notification = new Notification();
        notification.id = id;
        notification.className = className;
        notification.notificationOwnerId = notificationOwnerId;
        notification.fromUserId = fromUserId;
        notification.message = message;
        notification.notificationType = notificationType;
        notification.notificationData = notificationData;
        notification.notificationLink = notificationLink;
        notification.notificationApproveLink = notificationApproveLink;
        notification.notificationRejectLink = notificationRejectLink;
        notification.notificationImage = notificationImage;
        notification.notificationTitle = notificationTitle;
        notification.createdAt = createdAt;
        notification.notificationDataType = notificationDataType;
        notification.requestId = requestId;
        return notification;
    }

    public NotificationDataType getNotificationDataType()
    {
        return notificationDataType;
    }


    public UUID getRequestId()
    {
        return requestId;
    }
    public String getId()
    {
        return id;
    }

    public String getClassName()
    {
        return className;
    }

    public UUID getNotificationOwnerId()
    {
        return notificationOwnerId;
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

    public String getNotificationApproveLink()
    {
        return notificationApproveLink;
    }

    public String getNotificationRejectLink()
    {
        return notificationRejectLink;
    }

    public String getNotificationImage()
    {
        return notificationImage;
    }

    public String getNotificationTitle()
    {
        return notificationTitle;
    }

    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
}
