package callofproject.dev.task.config.kafka;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.task.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.task.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * This class represents a Kafka consumer service responsible for listening to messages from a Kafka topic.
 */
@Service
public class KafkaConsumer
{
    private final TaskServiceHelper m_serviceHelper;
    private final IUserMapper m_userMapper;

    /**
     * Constructs a new KafkaConsumer with the provided dependencies.
     *
     * @param serviceHelper The ProjectServiceHelper instance used for handling Kafka messages.
     * @param userMapper    The IUserMapper instance used for mapping UserDTO messages.
     */
    public KafkaConsumer(TaskServiceHelper serviceHelper, IUserMapper userMapper)
    {
        m_serviceHelper = serviceHelper;
        m_userMapper = userMapper;
    }

    /**
     * Listens to the specified Kafka topic and processes UserDTO messages.
     *
     * @param dto The UserDTO message received from Kafka.
     */
    @KafkaListener(topics = "${spring.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenAuthenticationTopic(UserKafkaDTO dto)
    {


        if (dto.operation() == EOperation.CREATE || dto.operation() == EOperation.UPDATE)
        {
            System.out.println("DTO: " + dto.username());
            m_serviceHelper.saveUser(m_userMapper.toUser(dto));
        }

        if (dto.operation() == EOperation.DELETE)
        {
            var user = m_serviceHelper.findUserById(dto.userId());
            if (user.isEmpty())
                return;
            user.get().setDeletedAt(dto.deletedAt());
            m_serviceHelper.saveUser(user.get());
        }

        if (dto.operation() == EOperation.REGISTER_NOT_VERIFY)
        {
            var user = m_serviceHelper.findUserById(dto.userId());
            if (user.isEmpty())
                return;
            m_serviceHelper.deleteUser(user.get());
        }
    }
}
