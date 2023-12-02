package callofproject.dev.nosql.entity;

import callofproject.dev.nosql.enums.NotificationType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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

    public Notification()
    {
    }

    public static class Builder
    {
        private final Notification m_notification;

        public Builder()
        {
            m_notification = new Notification();
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

    public Notification build() {
        Notification notification = new Notification();
        notification.id = id;
        notification.className = className;
        notification.notificationOwnerId = notificationOwnerId;
        notification.fromUserId = fromUserId;
        notification.message = message;
        notification.notificationType = notificationType;
        notification.notificationData = notificationData;
        notification.notificationLink = notificationLink;
        return notification;
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
}
