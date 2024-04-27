package callofproject.dev.service.notification.service;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.library.exception.ISupplier;
import callofproject.dev.nosql.dal.NotificationServiceHelper;
import callofproject.dev.nosql.entity.Notification;
import callofproject.dev.service.notification.dto.NotificationDTO;
import callofproject.dev.util.stream.StreamUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;

import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;
import static callofproject.dev.util.stream.StreamUtil.toStream;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.by;


/**
 * Service class for handling notification operations.
 */
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

        var list = new ArrayList<>(toStream(notifications)
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
                .toList());

        list.sort((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()));
        return new MultipleResponseMessagePageable<>(notificationPageable.getTotalPages(), page, notificationPageable.getNumberOfElements(),
                "Notifications found!", list);
    }

    public ResponseMessage<Object> markAllNotificationRead(UUID userId)
    {
        try
        {
            var allNotifications = m_notificationServiceHelper.findAllNotificationsByNotificationOwnerId(userId);
            allNotifications.forEach(notification -> notification.setIsRead(true));
            doForDataService(() -> m_notificationServiceHelper.saveAllNotifications(allNotifications), "NotificationService::markAllRead");
            return new ResponseMessage<>("All notifications are marked as read!", Status.OK, true);
        } catch (Throwable e)
        {
            return new ResponseMessage<>("An error occurred while marking all notifications as read!", Status.INTERNAL_SERVER_ERROR, false);
        }
    }


    public ResponseMessage<Object> countUnreadNotifications(UUID userId)
    {
        var notifications = doForDataService(() -> m_notificationServiceHelper.findAllUnreadNotificationsByNotificationOwnerId(userId),
                "NotificationService::countUnreadNotifications");


        return new ResponseMessage<>("Found notifications", Status.OK, StreamUtil.toStream(notifications).count());
    }

    public ResponseMessage<Object> clearAllNotificationsByNotificationOwnerId(UUID userId)
    {
        var response = doForDataService(() -> m_notificationServiceHelper.removeAllNotificationsByNotificationOwnerId(userId),
                "NotificationService::clearAllNotificationsByNotificationOwnerId");

        return new ResponseMessage<>(response ? "All notifications are cleared!" : "An error occurred while clearing all notifications!", Status.OK, response);
    }
}