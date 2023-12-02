package callofproject.dev.nosql.dal;

import callofproject.dev.nosql.entity.Notification;
import callofproject.dev.nosql.repository.INotificationRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForRepository;
import static callofproject.dev.nosql.NoSqlBeanName.NOTIFICATION_SERVICE_HELPER_BEAN_NAME;

/**
 * NotificationServiceHelper class represent the helper class of the NotificationService.
 * Copyleft (c) NoSQLRepository.
 * All Rights Free
 */
@Component(NOTIFICATION_SERVICE_HELPER_BEAN_NAME)
@Lazy
public class NotificationServiceHelper
{
    private final INotificationRepository m_notificationRepository;

    /**
     * Constructor
     *
     * @param notificationRepository notification repository
     */
    public NotificationServiceHelper(INotificationRepository notificationRepository)
    {
        m_notificationRepository = notificationRepository;
    }

    /**
     * Save notification
     *
     * @param notification notification
     * @return Notification object
     */
    public Notification saveNotification(Notification notification)
    {
        return doForRepository(() -> m_notificationRepository.save(notification), "NotificationServiceHelper::saveNotification");
    }

    /**
     * Remove notification
     *
     * @param notification notification
     * @return boolean value
     */
    public boolean removeNotification(Notification notification)
    {
        doForRepository(() -> m_notificationRepository.delete(notification), "NotificationServiceHelper::removeNotification");
        return true;
    }

    /**
     * Remove notification by notification owner id and notification id
     *
     * @param ownerId        notification owner id
     * @param notificationId notification id
     * @return boolean value
     */
    public boolean removeNotificationByNotificationOwnerIdAndNotificationId(UUID ownerId, String notificationId)
    {
        doForRepository(() -> m_notificationRepository.removeAllByNotificationOwnerIdAndId(ownerId, notificationId),
                "NotificationServiceHelper::removeNotificationByNotificationOwnerIdAndNotificationId");
        return true;
    }

    /**
     * Remove all notifications by notification owner id
     *
     * @param ownerId notification owner id
     * @return boolean value
     */
    public boolean removeAllNotificationsByNotificationOwnerId(UUID ownerId)
    {
        doForRepository(() -> m_notificationRepository.removeAllByNotificationOwnerId(ownerId), "NotificationServiceHelper::removeAllNotifications");
        return true;
    }

    /**
     * Find all notifications by notification owner id
     *
     * @param ownerId notification owner id
     * @return Iterable<Notification>
     */
    public Iterable<Notification> findAllNotificationsByNotificationOwnerId(UUID ownerId)
    {
        return doForRepository(() -> m_notificationRepository.findAllByNotificationOwnerId(ownerId), "NotificationServiceHelper::findAllNotificationsByNotificationOwnerId");
    }
}