package callofproject.dev.service.notification.config.service;

import callofproject.dev.nosql.dal.NotificationServiceHelper;
import callofproject.dev.nosql.entity.Notification;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;


@Service
@Lazy
public class NotificationService
{
    private final NotificationServiceHelper m_notificationServiceHelper;

    /**
     * Constructor.
     *
     * @param notificationServiceHelper represent the notification service helper.
     */
    public NotificationService(NotificationServiceHelper notificationServiceHelper)
    {
        m_notificationServiceHelper = notificationServiceHelper;
    }

    /**
     * Save notification.
     *
     * @param notification represent the notification.
     * @return Notification.
     */
    public Notification saveNotification(Notification notification)
    {
        return doForDataService(() -> m_notificationServiceHelper.saveNotification(notification), "NotificationService::saveNotification");
    }

    /**
     * Find notification by notification id.
     *
     * @param userId represent the user id.
     * @return boolean value which is true if notifications are removed.
     */
    public boolean removeAllNotificationsByNotificationOwnerId(UUID userId)
    {
        return doForDataService(() -> m_notificationServiceHelper.removeAllNotificationsByNotificationOwnerId(userId),
                "NotificationService::removeAllNotificationsByNotificationOwnerId");
    }

    /**
     * Remove notification by notification id.
     *
     * @param notificationId represent the notification id.
     * @param ownerId        represent the owner id.
     * @return boolean value which is true if notification is removed.
     */
    public boolean removeNotificationByNotificationOwnerIdAndNotificationId(UUID ownerId, String notificationId)
    {
        return doForDataService(() -> m_notificationServiceHelper.removeNotificationByNotificationOwnerIdAndNotificationId(ownerId, notificationId),
                "NotificationService::removeNotificationByNotificationOwnerIdAndNotificationId");
    }

    /**
     * Find all notifications by notification owner id.
     *
     * @param userId represent the user id.
     * @return Notifications.
     */
    public Iterable<Notification> findAllNotificationsByNotificationOwnerId(UUID userId)
    {
        return doForDataService(() -> m_notificationServiceHelper.findAllNotificationsByNotificationOwnerId(userId),
                "NotificationService::findAllNotificationsByNotificationOwnerId");
    }
}
