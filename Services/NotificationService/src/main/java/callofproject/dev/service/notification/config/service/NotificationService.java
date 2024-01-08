package callofproject.dev.service.notification.config.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.nosql.dal.NotificationServiceHelper;
import callofproject.dev.nosql.entity.Notification;
import callofproject.dev.service.notification.dto.NotificationDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;


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
    public MultipleResponseMessagePageable<Object> findAllNotificationsByNotificationOwnerIdAndSortCreatedAt(UUID userId, int page)
    {
        var sort = by(ASC, "createdAt");
        var pageable = PageRequest.of(page - 1, 15, sort);
        ISupplier<Page<Notification>> supplier = () -> m_notificationServiceHelper.findAllNotificationsByNotificationOwnerIdAndSortCreatedAt(userId, pageable);

        var notificationPageable = doForDataService(supplier, "NotificationService::findAllNotificationsByNotificationOwnerIdAndSortCreatedAt");
        var notifications = notificationPageable.getContent();

        var list = toStream(notifications)
                .map(notification -> new NotificationDTO.Builder()
                        .setNotificationData(notification.getNotificationData())
                        .setNotificationLink(notification.getNotificationLink())
                        .setNotificationType(notification.getNotificationType())
                        .setMessage(notification.getMessage())
                        .setFromUserId(notification.getFromUserId())
                        .setToUserId(notification.getNotificationOwnerId())
                        .setCreatedAt(notification.getCreatedAt())
                        .setNotificationImage(notification.getNotificationImage())
                        .setNotificationTitle(notification.getNotificationTitle())
                        .setRequestId(notification.getRequestId())
                        .setNotificationDataType(notification.getNotificationDataType())
                        .setNotificationApproveLink(notification.getNotificationApproveLink())
                        .setNotificationRejectLink(notification.getNotificationRejectLink())
                        .setNotificationId(notification.getId())
                        .build())
                .toList();


        return new MultipleResponseMessagePageable<>(notificationPageable.getTotalPages(), page, notificationPageable.getNumberOfElements(),
                "Notifications found!", list);
    }
}