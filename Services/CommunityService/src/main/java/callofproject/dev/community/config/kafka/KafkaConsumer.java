package callofproject.dev.community.config.kafka;

import callofproject.dev.community.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * This class represents a Kafka consumer service responsible for listening to messages from a Kafka topic.
 */
@Service
public class KafkaConsumer
{

    private final IUserRepository m_userRepository;
    private final IUserMapper m_userMapper;

    /**
     * Constructs a new KafkaConsumer with the provided dependencies.
     *
     * @param userMapper The IUserMapper instance used for mapping UserDTO messages.
     */
    public KafkaConsumer(IUserRepository userRepository, IUserMapper userMapper)
    {
        m_userRepository = userRepository;
        m_userMapper = userMapper;
    }

    /**
     * Listens to the specified Kafka topic and processes UserDTO messages.
     *
     * @param dto The UserDTO message received from Kafka.
     */
    @KafkaListener(
            topics = "${spring.kafka.user-topic-name}",
            groupId = "${spring.kafka.consumer.user-group-id}",
            containerFactory = "configUserKafkaListener"
    )
    public void listenAuthenticationTopic(UserKafkaDTO dto)
    {

        var user = m_userMapper.toUser(dto);
        m_userRepository.save(user);

     /*   if (dto.operation() == EOperation.CREATE || dto.operation() == EOperation.UPDATE)
        {

        }

        if (dto.operation() == EOperation.DELETE)
        {

        }

        if (dto.operation() == EOperation.REGISTER_NOT_VERIFY)
        {

        }*/
    }


}
