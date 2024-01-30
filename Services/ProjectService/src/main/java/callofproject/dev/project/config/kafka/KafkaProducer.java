package callofproject.dev.project.config.kafka;

import callofproject.dev.project.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.project.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.project.dto.ProjectParticipantNotificationDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import static org.springframework.kafka.support.KafkaHeaders.TOPIC;

/**
 * This class represents a Kafka producer service responsible for sending messages to a Kafka topic.
 */
@Service
public class KafkaProducer
{
    private final NewTopic m_notificationTopic;
    private final NewTopic m_projectInfoTopic;
    private final NewTopic m_projectParticipantTopic;
    private final KafkaTemplate<String, ProjectParticipantNotificationDTO> m_projectParticipantKafkaTemplate;
    private final KafkaTemplate<String, ProjectInfoKafkaDTO> m_projectInfoKafkaTemplate;
    private final KafkaTemplate<String, ProjectParticipantKafkaDTO> m_participantKafkaDTOKafkaTemplate;

    /**
     * Constructs a new KafkaProducer with the provided dependencies.
     *
     * @param notificationTopic The NewTopic instance representing the Kafka topic to send messages to.
     * @param kafkaTemplate     The KafkaTemplate instance for sending messages to the Kafka topic.
     */
    public KafkaProducer(@Qualifier("notificationTopic") NewTopic notificationTopic,
                         @Qualifier("projectInfoTopic") NewTopic projectInfoTopic,
                         @Qualifier("projectParticipantTopic") NewTopic projectParticipantTopic,
                         KafkaTemplate<String, ProjectParticipantNotificationDTO> kafkaTemplate,
                         KafkaTemplate<String, ProjectInfoKafkaDTO> projectInfoKafkaTemplate,
                         KafkaTemplate<String, ProjectParticipantKafkaDTO> participantKafkaDTOKafkaTemplate)
    {
        m_projectParticipantTopic = projectParticipantTopic;
        m_notificationTopic = notificationTopic;
        m_projectInfoTopic = projectInfoTopic;
        m_projectInfoKafkaTemplate = projectInfoKafkaTemplate;
        m_projectParticipantKafkaTemplate = kafkaTemplate;
        m_participantKafkaDTOKafkaTemplate = participantKafkaDTOKafkaTemplate;
    }

    /**
     * Sends a ProjectParticipantNotificationDTO message to the Kafka topic.
     *
     * @param message The message to send.
     */
    public void sendProjectParticipantNotification(ProjectParticipantNotificationDTO message)
    {
        var msg = MessageBuilder
                .withPayload(message)
                .setHeader(TOPIC, m_notificationTopic.name())
                .build();

        m_projectParticipantKafkaTemplate.send(msg);
    }


    /**
     * Sends a ProjectInfoKafkaDTO message to the Kafka topic.
     *
     * @param projectKafkaDTO The message to send.
     */
    public void sendProjectInfo(ProjectInfoKafkaDTO projectKafkaDTO)
    {
        var msg = MessageBuilder
                .withPayload(projectKafkaDTO)
                .setHeader(TOPIC, m_projectInfoTopic.name())
                .build();

        m_projectInfoKafkaTemplate.send(msg);
    }


    /**
     * Sends a ProjectInfoKafkaDTO message to the Kafka topic.
     *
     * @param projectKafkaDTO The message to send.
     */
    public void sendProjectParticipant(ProjectParticipantKafkaDTO projectKafkaDTO)
    {
        var msg = MessageBuilder
                .withPayload(projectKafkaDTO)
                .setHeader(TOPIC, m_projectParticipantTopic.name())
                .build();

        m_participantKafkaDTOKafkaTemplate.send(msg);
    }
}