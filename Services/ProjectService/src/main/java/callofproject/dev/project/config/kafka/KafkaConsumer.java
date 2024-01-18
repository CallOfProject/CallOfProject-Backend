package callofproject.dev.project.config.kafka;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.project.dto.UserDTO;
import callofproject.dev.project.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * This class represents a Kafka consumer service responsible for listening to messages from a Kafka topic.
 */
@Service
public class KafkaConsumer
{
    private final ProjectServiceHelper m_serviceHelper;
    private final IUserMapper m_userMapper;

    /**
     * Constructs a new KafkaConsumer with the provided dependencies.
     *
     * @param serviceHelper The ProjectServiceHelper instance used for handling Kafka messages.
     * @param userMapper    The IUserMapper instance used for mapping UserDTO messages.
     */
    public KafkaConsumer(ProjectServiceHelper serviceHelper, IUserMapper userMapper)
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
    public void listenAuthenticationTopic(UserDTO dto)
    {


        if (dto.operation() == EOperation.CREATE || dto.operation() == EOperation.UPDATE)
            m_serviceHelper.addUser(m_userMapper.toUser(dto));

        if (dto.operation() == EOperation.DELETE)
        {
            var user = m_serviceHelper.findUserById(dto.userId());
            if (user.isEmpty())
                return;
            user.get().setDeletedAt(dto.deletedAt());
            m_serviceHelper.addUser(user.get());
        }

        if (dto.operation() == EOperation.REGISTER_NOT_VERIFY)
        {
            var user = m_serviceHelper.findUserById(dto.userId());
            if (user.isEmpty())
                return;
            m_serviceHelper.removeUser(user.get().getUserId());
        }
    }
}
