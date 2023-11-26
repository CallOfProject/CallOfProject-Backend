package callofproject.dev.project.config.kafka;

import callofproject.dev.project.dto.UserDTO;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Lazy
public class KafkaConsumer
{
    @KafkaListener(topics = "${spring.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void consumeMessage(UserDTO message)
    {
        System.out.println(message);
    }
}
