package callofproject.dev.task.config.kafka;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.ProjectParticipant;
import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.task.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.task.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.task.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.task.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

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
    @KafkaListener(
            topics = "${spring.kafka.user-topic-name}",
            groupId = "${spring.kafka.consumer.user-group-id}",
            containerFactory = "configUserKafkaListener"
    )
    public void listenAuthenticationTopic(UserKafkaDTO dto)
    {
        if (dto.operation() == EOperation.CREATE || dto.operation() == EOperation.UPDATE)
            m_serviceHelper.saveUser(m_userMapper.toUser(dto));

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


    @KafkaListener(
            topics = "${spring.kafka.project-info-topic-name}",
            groupId = "${spring.kafka.consumer.project-info-group-id}",
            containerFactory = "configProjectInfoKafkaListener"
    )
    public void consumeStockInfo(ProjectInfoKafkaDTO projectDTO)
    {
        var owner = m_serviceHelper.findUserById(projectDTO.projectOwner().userId());
        var project = new Project(projectDTO.projectId(), projectDTO.projectName(), owner.get());
        var savedProject = m_serviceHelper.saveProject(project);
        var participants = projectDTO.projectParticipants().stream().map(pp -> toProjectParticipant(pp, savedProject)).collect(Collectors.toSet());
        savedProject.setProjectParticipants(participants);
        savedProject.setProjectStatus(projectDTO.projectStatus());
        savedProject.setAdminOperationStatus(AdminOperationStatus.valueOf(projectDTO.adminOperationStatus().name()));
        m_serviceHelper.saveProject(savedProject);
    }


    @KafkaListener(
            topics = "${spring.kafka.project-participant-topic-name}",
            groupId = "${spring.kafka.consumer.project-participant-group-id}",
            containerFactory = "configProjectParticipantKafkaListener"
    )
    public void consumeProjectParticipant(ProjectParticipantKafkaDTO dto)
    {
        var project = m_serviceHelper.findProjectById(dto.projectId());
        var user = m_serviceHelper.findUserById(dto.userId());

        if (project.isEmpty() || user.isEmpty())
            return;

        var participant = new ProjectParticipant(dto.projectParticipantId(), project.get(), user.get(), dto.joinDate());
        m_serviceHelper.saveParticipant(participant);
    }

    private ProjectParticipant toProjectParticipant(ProjectParticipantKafkaDTO participant, Project project)
    {
        var user = m_serviceHelper.findUserById(participant.userId());
        return new ProjectParticipant(project, user.get());
    }
}
