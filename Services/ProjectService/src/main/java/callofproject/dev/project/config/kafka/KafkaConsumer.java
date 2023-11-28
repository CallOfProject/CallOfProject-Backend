package callofproject.dev.project.config.kafka;

import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.project.dto.Operation;
import callofproject.dev.project.dto.UserDTO;
import callofproject.dev.project.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Service
public class KafkaConsumer
{
    //Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);
    private final ProjectServiceHelper m_serviceHelper;
    private final IUserMapper m_userMapper;

    public KafkaConsumer(ProjectServiceHelper serviceHelper, IUserMapper userMapper)
    {
        m_serviceHelper = serviceHelper;
        m_userMapper = userMapper;
    }

    @KafkaListener(topics = "${spring.kafka.topic-name}", groupId = "${spring.kafka.consumer.group-id}")
    public void listenAuthenticationTopic(UserDTO dto)
    {
        if (dto.operation() == Operation.CREATE || dto.operation() == Operation.UPDATE)
            m_serviceHelper.addUser(m_userMapper.toUser(dto));

        else m_serviceHelper.removeUser(dto.userId());
    }
}
