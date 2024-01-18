package callofproject.dev.service.notification.dto;

import callofproject.dev.nosql.enums.NotificationDataType;
import callofproject.dev.nosql.enums.NotificationType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.UUID;


/**
 * DTO class for notification user response.
 */
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


    /**
     * Constructor.
     */
    private NotificationDTO()
    {
    }


    /**
     * Builder class for notification DTO.
     */
    public static class Builder
    {
        private final NotificationDTO m_notificationDTO;

        /**
         * Constructor.
         */
        public Builder()
        {
            m_notificationDTO = new NotificationDTO();
        }


        /**
         * Set Notification id.
         *
         * @param notificationId represent the notification id.
         * @return Builder.
         */
        public Builder setNotificationId(String notificationId)
        {
            m_notificationDTO.notificationId = notificationId;
            return this;
        }


        /**
         * Set approval link.
         *
         * @param approveLink represent the approval link.
         * @return Builder.
         */
        public Builder setNotificationApproveLink(String approveLink)
        {
            m_notificationDTO.notificationApproveLink = approveLink;
            return this;
        }


        /**
         * Set reject link.
         *
         * @param rejectLink represent the reject link.
         * @return Builder.
         */
        public Builder setNotificationRejectLink(String rejectLink)
        {
            m_notificationDTO.notificationRejectLink = rejectLink;
            return this;
        }


        /**
         * Set request id.
         *
         * @param requestId represent the request id.
         * @return Builder.
         */
        public Builder setRequestId(UUID requestId)
        {
            m_notificationDTO.requestId = requestId;
            return this;
        }


        /**
         * Set notification data type.
         *
         * @param notificationDataType represent the notification data type.
         * @return Builder.
         */
        public Builder setNotificationDataType(NotificationDataType notificationDataType)
        {
            m_notificationDTO.notificationDataType = notificationDataType;
            return this;
        }


        /**
         * Set Created at.
         *
         * @param createdAt represent the created at.
         * @return Builder.
         */
        public Builder setCreatedAt(LocalDateTime createdAt)
        {
            m_notificationDTO.createdAt = createdAt;
            return this;
        }


        /**
         * Set notification title.
         *
         * @param notificationTitle represent the notification title.
         * @return Builder.
         */
        public Builder setNotificationTitle(String notificationTitle)
        {
            m_notificationDTO.notificationTitle = notificationTitle;
            return this;
        }


        /**
         * Set notification image.
         *
         * @param notificationImage represent the notification image.
         * @return Builder.
         */
        public Builder setNotificationImage(String notificationImage)
        {
            m_notificationDTO.notificationImage = notificationImage;
            return this;
        }


        /**
         * Set user id.
         *
         * @param toUserId represent the user id.
         * @return Builder.
         */
        public Builder setToUserId(UUID toUserId)
        {
            m_notificationDTO.toUserId = toUserId;
            return this;
        }


        /**
         * Set from user id.
         *
         * @param fromUserId represent the from user id.
         * @return Builder.
         */
        public Builder setFromUserId(UUID fromUserId)
        {
            m_notificationDTO.fromUserId = fromUserId;
            return this;
        }


        /**
         * Set message.
         *
         * @param message represent the message.
         * @return Builder.
         */
        public Builder setMessage(String message)
        {
            m_notificationDTO.message = message;
            return this;
        }


        /**
         * Set notification type.
         *
         * @param notificationType represent the notification type.
         * @return Builder.
         */
        public Builder setNotificationType(NotificationType notificationType)
        {
            m_notificationDTO.notificationType = notificationType;
            return this;
        }


        /**
         * Set notification data.
         *
         * @param notificationData represent the notification data.
         * @return Builder.
         */
        public Builder setNotificationData(Object notificationData)
        {
            m_notificationDTO.notificationData = notificationData;
            return this;
        }


        /**
         * Set notification link.
         *
         * @param notificationLink represent the notification link.
         * @return Builder.
         */
        public Builder setNotificationLink(String notificationLink)
        {
            m_notificationDTO.notificationLink = notificationLink;
            return this;
        }


        /**
         * Build notification DTO.
         *
         * @return NotificationDTO.
         */
        public NotificationDTO build()
        {
            return m_notificationDTO;
        }
    }


    /**
     * Get notification id.
     *
     * @return String.
     */
    public String getNotificationId()
    {
        return notificationId;
    }


    /**
     * Get request id.
     *
     * @return UUID.
     */
    public UUID getRequestId()
    {
        return requestId;
    }


    /**
     * Get notification data type.
     *
     * @return NotificationDataType.
     */
    public NotificationDataType getNotificationDataType()
    {
        return notificationDataType;
    }


    /**
     * Get notification image.
     *
     * @return String.
     */
    public String getNotificationImage()
    {
        return notificationImage;
    }


    /**
     * Get notification approve link.
     *
     * @return String.
     */
    public String getNotificationApproveLink()
    {
        return notificationApproveLink;
    }


    /**
     * Get notification reject link.
     *
     * @return String.
     */
    public String getNotificationRejectLink()
    {
        return notificationRejectLink;
    }


    /**
     * Get notification title.
     *
     * @return String.
     */
    public String getNotificationTitle()
    {
        return notificationTitle;
    }


    /**
     * Get created at.
     *
     * @return LocalDateTime.
     */
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }


    /**
     * Set to user id.
     *
     * @param toUserId represent the to user id.
     */
    public void setToUserId(UUID toUserId)
    {
        this.toUserId = toUserId;
    }


    /**
     * Set notification id.
     *
     * @param notificationId represent the notification id.
     */
    public void setNotificationId(String notificationId)
    {
        this.notificationId = notificationId;
    }

    /**
     * Set from user id.
     *
     * @param fromUserId represent the from user id.
     */
    public void setFromUserId(UUID fromUserId)
    {
        this.fromUserId = fromUserId;
    }


    /**
     * Set message.
     *
     * @param message represent the message.
     */
    public void setMessage(String message)
    {
        this.message = message;
    }


    /**
     * Set notification image.
     *
     * @param notificationImage represent the notification image.
     */
    public void setNotificationImage(String notificationImage)
    {
        this.notificationImage = notificationImage;
    }


    /**
     * Set notification title.
     *
     * @param notificationTitle represent the notification title.
     */
    public void setNotificationTitle(String notificationTitle)
    {
        this.notificationTitle = notificationTitle;
    }


    /**
     * Set created at.
     *
     * @param createdAt represent the created at.
     */
    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }


    /**
     * Set notification data type.
     *
     * @param notificationDataType represent the notification data type.
     */
    public void setNotificationDataType(NotificationDataType notificationDataType)
    {
        this.notificationDataType = notificationDataType;
    }


    /**
     * Set request id.
     *
     * @param requestId represent the request id.
     */
    public void setRequestId(UUID requestId)
    {
        this.requestId = requestId;
    }


    /**
     * Set notification approve link.
     *
     * @param notificationApproveLink represent the notification approve link.
     */
    public void setNotificationApproveLink(String notificationApproveLink)
    {
        this.notificationApproveLink = notificationApproveLink;
    }


    /**
     * Set notification reject link.
     *
     * @param notificationRejectLink represent the notification reject link.
     */
    public void setNotificationRejectLink(String notificationRejectLink)
    {
        this.notificationRejectLink = notificationRejectLink;
    }


    /**
     * Set notification type.
     *
     * @param notificationType represent the notification type.
     */
    public void setNotificationType(NotificationType notificationType)
    {
        this.notificationType = notificationType;
    }


    /**
     * Set notification data.
     *
     * @param notificationData represent the notification data.
     */
    public void setNotificationData(Object notificationData)
    {
        this.notificationData = notificationData;
    }


    /**
     * Set notification link.
     *
     * @param notificationLink represent the notification link.
     */
    public void setNotificationLink(String notificationLink)
    {
        this.notificationLink = notificationLink;
    }


    /**
     * Get to user id.
     *
     * @return UUID.
     */
    public UUID getToUserId()
    {
        return toUserId;
    }


    /**
     * Get from user id.
     *
     * @return UUID.
     */
    public UUID getFromUserId()
    {
        return fromUserId;
    }


    /**
     * Get message.
     *
     * @return String.
     */
    public String getMessage()
    {
        return message;
    }


    /**
     * Get notification type.
     *
     * @return NotificationType.
     */
    public NotificationType getNotificationType()
    {
        return notificationType;
    }


    /**
     * Get notification data.
     *
     * @return Object.
     */
    public Object getNotificationData()
    {
        return notificationData;
    }


    /**
     * Get notification link.
     *
     * @return String.
     */
    public String getNotificationLink()
    {
        return notificationLink;
    }
}
