package callofproject.dev.task.dto;


import callofproject.dev.data.common.enums.NotificationDataType;
import callofproject.dev.data.common.enums.NotificationType;

import java.util.UUID;

/**
 * This class represents a Data Transfer Object (DTO) for project participant notifications.
 * It is used to encapsulate information related to notifications sent to project participants.
 */
public class NotificationKafkaDTO
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

    /**
     * Private constructor to prevent direct instantiation.
     * Instances of this class should be created using the builder pattern.
     */
    private NotificationKafkaDTO()
    {
    }

    /**
     * Builder class for creating instances of {@link NotificationKafkaDTO}.
     */
    public static class Builder
    {
        private final NotificationKafkaDTO m_notificationKafkaDTO;

        /**
         * Constructs a new builder instance.
         */
        public Builder()
        {
            m_notificationKafkaDTO = new NotificationKafkaDTO();
        }

        /**
         * Sets the notification title.
         *
         * @param notificationTitle The title of the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationTitle(String notificationTitle)
        {
            m_notificationKafkaDTO.notificationTitle = notificationTitle;
            return this;
        }

        /**
         * Sets the notification data type.
         *
         * @param notificationDataType The data type of the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationDataType(NotificationDataType notificationDataType)
        {
            m_notificationKafkaDTO.notificationDataType = notificationDataType;
            return this;
        }

        /**
         * Sets the request ID associated with the notification.
         *
         * @param requestId The ID of the associated request.
         * @return The builder instance for method chaining.
         */
        public Builder setRequestId(UUID requestId)
        {
            m_notificationKafkaDTO.requestId = requestId;
            return this;
        }

        /**
         * Sets the notification image.
         *
         * @param notificationImage The image associated with the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationImage(String notificationImage)
        {
            m_notificationKafkaDTO.notificationImage = notificationImage;
            return this;
        }

        /**
         * Sets the approval link for the notification.
         *
         * @param approveLink The approval link associated with the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setApproveLink(String approveLink)
        {
            m_notificationKafkaDTO.notificationApproveLink = approveLink;
            return this;
        }

        /**
         * Sets the rejection link for the notification.
         *
         * @param rejectLink The rejection link associated with the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setRejectLink(String rejectLink)
        {
            m_notificationKafkaDTO.notificationRejectLink = rejectLink;
            return this;
        }

        /**
         * Sets the recipient user's ID.
         *
         * @param toUserId The recipient user's ID.
         * @return The builder instance for method chaining.
         */
        public Builder setToUserId(UUID toUserId)
        {
            m_notificationKafkaDTO.toUserId = toUserId;
            return this;
        }

        /**
         * Sets the sender user's ID.
         *
         * @param fromUserId The sender user's ID.
         * @return The builder instance for method chaining.
         */
        public Builder setFromUserId(UUID fromUserId)
        {
            m_notificationKafkaDTO.fromUserId = fromUserId;
            return this;
        }

        /**
         * Sets the notification message.
         *
         * @param message The notification message.
         * @return The builder instance for method chaining.
         */
        public Builder setMessage(String message)
        {
            m_notificationKafkaDTO.message = message;
            return this;
        }

        /**
         * Sets the notification type.
         *
         * @param notificationType The type of the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationType(NotificationType notificationType)
        {
            m_notificationKafkaDTO.notificationType = notificationType;
            return this;
        }

        /**
         * Sets the notification data.
         *
         * @param notificationData The data associated with the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationData(Object notificationData)
        {
            m_notificationKafkaDTO.notificationData = notificationData;
            return this;
        }

        /**
         * Sets the notification link.
         *
         * @param notificationLink The link associated with the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationLink(String notificationLink)
        {
            m_notificationKafkaDTO.notificationLink = notificationLink;
            return this;
        }

        /**
         * Builds and returns the final {@link NotificationKafkaDTO} instance.
         *
         * @return The built DTO instance.
         */
        public NotificationKafkaDTO build()
        {
            return m_notificationKafkaDTO;
        }
    }

    /**
     * Gets the notification data type.
     *
     * @return The data type of the notification.
     */
    public NotificationDataType getNotificationDataType()
    {
        return notificationDataType;
    }

    /**
     * Gets the request ID associated with the notification.
     *
     * @return The ID of the associated request.
     */
    public UUID getRequestId()
    {
        return requestId;
    }

    /**
     * Gets the recipient user's ID.
     *
     * @return The recipient user's ID.
     */
    public UUID getToUserId()
    {
        return toUserId;
    }

    /**
     * Gets the sender user's ID.
     *
     * @return The sender user's ID.
     */
    public UUID getFromUserId()
    {
        return fromUserId;
    }

    /**
     * Gets the notification message.
     *
     * @return The notification message.
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Gets the notification type.
     *
     * @return The type of the notification.
     */
    public NotificationType getNotificationType()
    {
        return notificationType;
    }

    /**
     * Gets the notification data.
     *
     * @return The data associated with the notification.
     */
    public Object getNotificationData()
    {
        return notificationData;
    }

    /**
     * Gets the notification link.
     *
     * @return The link associated with the notification.
     */
    public String getNotificationLink()
    {
        return notificationLink;
    }

    /**
     * Gets the notification image.
     *
     * @return The image associated with the notification.
     */
    public String getNotificationImage()
    {
        return notificationImage;
    }

    /**
     * Gets the notification title.
     *
     * @return The title of the notification.
     */
    public String getNotificationTitle()
    {
        return notificationTitle;
    }

    /**
     * Gets the notification approve link.
     *
     * @return The approval link associated with the notification.
     */
    public String getNotificationApproveLink()
    {
        return notificationApproveLink;
    }

    /**
     * Gets the notification reject link.
     *
     * @return The rejection link associated with the notification.
     */
    public String getNotificationRejectLink()
    {
        return notificationRejectLink;
    }
}
