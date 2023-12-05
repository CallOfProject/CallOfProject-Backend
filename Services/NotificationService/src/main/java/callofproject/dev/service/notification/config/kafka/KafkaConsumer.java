package callofproject.dev.service.notification.config.kafka;

import callofproject.dev.nosql.entity.Notification;
import callofproject.dev.service.notification.config.service.NotificationService;
import callofproject.dev.service.notification.dto.NotificationUserResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer
{
    private final NotificationService m_notificationService;


    public KafkaConsumer(NotificationService notificationService)
    {
        m_notificationService = notificationService;
    }

    @KafkaListener(topics = "${spring.kafka.consumer.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void projectServiceListener(NotificationUserResponseDTO message)
    {
        System.out.println("Received Message" + message);

        var notification = new Notification.Builder()
                .setMessage(message.message())
                .setFromUserId(message.fromUserId())
                .setNotificationOwnerId(message.toUserId())
                .setNotificationType(message.notificationType())
                .setNotificationLink(message.notificationLink())
                .setNotificationData(message.notificationData())
                .build();

        m_notificationService.saveNotification(notification);
    }
}
