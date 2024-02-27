package callofproject.dev.service.interview.config.kafka;

import callofproject.dev.service.interview.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.service.interview.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.service.interview.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.service.interview.data.dal.InterviewServiceHelper;
import callofproject.dev.service.interview.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * This class represents a Kafka consumer service responsible for listening to messages from a Kafka topic.
 */
@Service
public class KafkaConsumer
{
    private final IUserMapper m_userMapper;
    private final InterviewServiceHelper m_serviceHelper;

    public KafkaConsumer(IUserMapper userMapper, InterviewServiceHelper serviceHelper)
    {
        m_userMapper = userMapper;
        m_serviceHelper = serviceHelper;
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
    public void consumeAuthenticationTopic(UserKafkaDTO dto)
    {
        m_serviceHelper.saveUser(m_userMapper.toUser(dto));
    }


    @KafkaListener(
            topics = "${spring.kafka.project-info-topic-name}",
            groupId = "${spring.kafka.consumer.project-info-group-id}",
            containerFactory = "configProjectInfoKafkaListener"
    )
    public void consumeProjectInfo(ProjectInfoKafkaDTO projectDTO)
    {

    }


    @KafkaListener(
            topics = "${spring.kafka.project-participant-topic-name}",
            groupId = "${spring.kafka.consumer.project-participant-group-id}",
            containerFactory = "configProjectParticipantKafkaListener"
    )
    public void consumeProjectParticipant(ProjectParticipantKafkaDTO dto)
    {

    }

}
