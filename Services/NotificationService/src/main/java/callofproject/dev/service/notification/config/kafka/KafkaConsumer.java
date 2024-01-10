package callofproject.dev.service.notification.config.kafka;

import callofproject.dev.nosql.entity.Notification;
import callofproject.dev.service.notification.service.NotificationService;
import callofproject.dev.service.notification.dto.NotificationDTO;
import callofproject.dev.service.notification.dto.NotificationUserResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer
{
    private final NotificationService m_notificationService;
    private final SimpMessagingTemplate messagingTemplate;


    public KafkaConsumer(NotificationService notificationService, SimpMessagingTemplate messagingTemplate)
    {
        m_notificationService = notificationService;
        this.messagingTemplate = messagingTemplate;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void projectServiceListener(NotificationUserResponseDTO message)
    {
        var notification = new Notification.Builder()
                .setMessage(message.message())
                .setFromUserId(message.fromUserId())
                .setNotificationOwnerId(message.toUserId())
                .setNotificationType(message.notificationType())
                .setNotificationLink(message.notificationLink())
                .setNotificationData(message.notificationData())
                .setNotificationImage(message.notificationImage())
                .setNotificationTitle(message.notificationTitle())
                .setNotificationDataType(message.notificationDataType())
                .setNotificationApproveLink(message.notificationApproveLink())
                .setNotificationRejectLink(message.notificationRejectLink())
                .setRequestId(message.requestId())
                .build();

        var savedNotification = m_notificationService.saveNotification(notification);

        sendNotificationToUser(savedNotification);
    }

    private void sendNotificationToUser(Notification savedNotification)
    {
        var dto = new NotificationDTO.Builder()
                .setNotificationData(savedNotification.getNotificationData())
                .setNotificationLink(savedNotification.getNotificationLink())
                .setNotificationType(savedNotification.getNotificationType())
                .setMessage(savedNotification.getMessage())
                .setFromUserId(savedNotification.getFromUserId())
                .setToUserId(savedNotification.getNotificationOwnerId())
                .setCreatedAt(savedNotification.getCreatedAt())
                .setNotificationImage(savedNotification.getNotificationImage())
                .setNotificationTitle(savedNotification.getNotificationTitle())
                .setRequestId(savedNotification.getRequestId())
                .setNotificationDataType(savedNotification.getNotificationDataType())
                .setNotificationApproveLink(savedNotification.getNotificationApproveLink())
                .setNotificationRejectLink(savedNotification.getNotificationRejectLink())
                .setNotificationId(savedNotification.getId())
                .build();

        messagingTemplate.convertAndSend("/topic/user-" + dto.getToUserId(), dto);
    }
}
