package callofproject.dev.community.config.kafka;

import callofproject.dev.community.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.community.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.community.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.entity.Community;
import callofproject.dev.data.community.entity.Project;
import callofproject.dev.data.community.entity.ProjectParticipant;
import callofproject.dev.data.community.entity.enumeration.AdminOperationStatus;
import callofproject.dev.data.community.repository.IProjectRepository;
import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * This class represents a Kafka consumer service responsible for listening to messages from a Kafka topic.
 */
@Service
public class KafkaConsumer
{
    private final IUserRepository m_userRepository;
    private final IUserMapper m_userMapper;
    private final IProjectRepository m_projectRepository;
    private final CommunityServiceHelper m_serviceHelper;

    /**
     * Constructs a new KafkaConsumer with the provided dependencies.
     *
     * @param userMapper The IUserMapper instance used for mapping UserDTO messages.
     */
    public KafkaConsumer(IUserRepository userRepository, IUserMapper userMapper, IProjectRepository projectRepository, CommunityServiceHelper serviceHelper)
    {
        m_userRepository = userRepository;
        m_userMapper = userMapper;
        m_projectRepository = projectRepository;
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
    public void listenAuthenticationTopic(UserKafkaDTO dto)
    {

        var user = m_userMapper.toUser(dto);
        m_userRepository.save(user);
    }

    @KafkaListener(
            topics = "${spring.kafka.project-topic-name}",
            groupId = "${spring.kafka.consumer.project-group-id}",
            containerFactory = "configProjectInfoKafkaListener"
    )
    public synchronized void consumeProjectInfo(ProjectInfoKafkaDTO projectDTO)
    {
        var community = m_serviceHelper.upsertCommunity(new Community(projectDTO.projectName() + "-" + projectDTO.projectId()));

        var owner = m_serviceHelper.findUserById(projectDTO.projectOwner().userId());
        var project = new Project(projectDTO.projectId(), projectDTO.projectName(), owner.get());
        //var savedProject = m_projectRepository.save(project);
        var participants = projectDTO.projectParticipants().stream().map(pp -> toProjectParticipant(pp, project)).collect(Collectors.toSet());
        project.setProjectParticipants(participants);
        project.setProjectStatus(projectDTO.projectStatus());
        project.setAdminOperationStatus(AdminOperationStatus.valueOf(projectDTO.adminOperationStatus().name()));
        project.setCommunity(community);
        m_projectRepository.save(project);
    }

    private ProjectParticipant toProjectParticipant(ProjectParticipantKafkaDTO participant, Project project)
    {
        var user = m_serviceHelper.findUserById(participant.userId());
        return new ProjectParticipant(project, user.get());
    }
}
