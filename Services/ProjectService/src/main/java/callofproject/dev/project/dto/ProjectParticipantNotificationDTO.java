package callofproject.dev.project.dto;

import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;

import java.util.UUID;

/**
 * This class represents a Data Transfer Object (DTO) for project participant notifications.
 * It is used to encapsulate information related to notifications sent to project participants.
 */
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

    /**
     * Private constructor to prevent direct instantiation.
     * Instances of this class should be created using the builder pattern.
     */
    private ProjectParticipantNotificationDTO()
    {
    }

    /**
     * Builder class for creating instances of {@link ProjectParticipantNotificationDTO}.
     */
    public static class Builder
    {
        private final ProjectParticipantNotificationDTO m_projectParticipantNotificationDTO;

        /**
         * Constructs a new builder instance.
         */
        public Builder()
        {
            m_projectParticipantNotificationDTO = new ProjectParticipantNotificationDTO();
        }

        /**
         * Sets the notification title.
         *
         * @param notificationTitle The title of the notification.
         * @return The builder instance for method chaining.
         */
        public Builder setNotificationTitle(String notificationTitle)
        {
            m_projectParticipantNotificationDTO.notificationTitle = notificationTitle;
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
            m_projectParticipantNotificationDTO.notificationDataType = notificationDataType;
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
            m_projectParticipantNotificationDTO.requestId = requestId;
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
            m_projectParticipantNotificationDTO.notificationImage = notificationImage;
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
            m_projectParticipantNotificationDTO.notificationApproveLink = approveLink;
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
            m_projectParticipantNotificationDTO.notificationRejectLink = rejectLink;
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
            m_projectParticipantNotificationDTO.toUserId = toUserId;
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
            m_projectParticipantNotificationDTO.fromUserId = fromUserId;
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
            m_projectParticipantNotificationDTO.message = message;
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
            m_projectParticipantNotificationDTO.notificationType = notificationType;
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
            m_projectParticipantNotificationDTO.notificationData = notificationData;
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
            m_projectParticipantNotificationDTO.notificationLink = notificationLink;
            return this;
        }

        /**
         * Builds and returns the final {@link ProjectParticipantNotificationDTO} instance.
         *
         * @return The built DTO instance.
         */
        public ProjectParticipantNotificationDTO build()
        {
            return m_projectParticipantNotificationDTO;
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
