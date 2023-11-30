package callofproject.dev.service.notification.config.kafka;

import callofproject.dev.service.notification.dto.NotificationUserResponseDTO;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer
{
    @KafkaListener(topics = "${spring.kafka.consumer.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void projectServiceListener(NotificationUserResponseDTO message)
    {
        System.out.println("Received Message in group foo: " + message);
    }
}
