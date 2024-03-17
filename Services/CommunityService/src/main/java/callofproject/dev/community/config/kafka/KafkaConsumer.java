package callofproject.dev.community.config.kafka;

import callofproject.dev.community.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.community.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.community.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
import callofproject.dev.data.community.entity.Community;
import callofproject.dev.data.community.entity.Project;
import callofproject.dev.data.community.entity.ProjectParticipant;
import callofproject.dev.data.community.entity.User;
import callofproject.dev.data.community.entity.enumeration.AdminOperationStatus;
import callofproject.dev.data.community.repository.IProjectRepository;
import callofproject.dev.data.community.repository.IUserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;
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

        switch (projectDTO.operation())
        {
            case CREATE, UPDATE -> createProject(projectDTO);
            case DELETE -> removeProject(projectDTO.projectId());
            case SOFT_DELETED -> softDeleteProject(projectDTO.projectId());
        }

    }

    @KafkaListener(topics = "${spring.kafka.project-participant-topic-name}", groupId = "${spring.kafka.consumer.project-participant-group-id}", containerFactory = "configProjectParticipantKafkaListener")
    @Transactional
    public void consumeProjectParticipant(ProjectParticipantKafkaDTO dto)
    {
        var project = m_serviceHelper.findProjectById(dto.projectId());
        var user = m_serviceHelper.findUserById(dto.userId());

        if (project.isEmpty() || user.isEmpty())
            return;

        if (!dto.isDeleted())
            addParticipant(dto, project.get(), user.get());

        else removeParticipant(project.get(), user.get());
    }


    private void removeUser(UUID userId)
    {
        var user = m_serviceHelper.findUserById(userId);
        if (user.isEmpty())
            return;
        m_serviceHelper.deleteUser(user.get());
    }

    private void softDeleteUser(UserKafkaDTO dto)
    {
        var user = m_serviceHelper.findUserById(dto.userId());
        if (user.isEmpty())
            return;
        user.get().setDeletedAt(dto.deletedAt());
        m_serviceHelper.upsertUser(user.get());
    }

    private void removeParticipant(Project project, User user)
    {
        var participant = m_serviceHelper.findProjectParticipantByProjectIdAndUserId(project.getProjectId(), user.getUserId());

        if (participant.isEmpty())
            return;

        project.getProjectParticipants().remove(participant.get());
        user.getProjectParticipants().remove(participant.get());
        m_serviceHelper.removeParticipant(participant.get());
    }

    private void addParticipant(ProjectParticipantKafkaDTO dto, Project project, User user)
    {
        var participant = new ProjectParticipant(dto.projectParticipantId(), project, user, dto.joinDate());
        m_serviceHelper.saveParticipant(participant);
    }

    private ProjectParticipant toProjectParticipant(ProjectParticipantKafkaDTO participant, Project project)
    {
        return new ProjectParticipant(project, m_serviceHelper.findUserById(participant.userId()).get());
    }

    private void removeProject(UUID projectId)
    {
        var project = m_serviceHelper.findProjectById(projectId);

        if (project.isEmpty())
            return;

        project.get().getProjectParticipants().forEach(pp -> removeParticipant(project.get(), pp.getUser()));
        m_serviceHelper.deleteProjectById(projectId);
    }

    private void createProject(ProjectInfoKafkaDTO projectDTO)
    {
        var owner = m_serviceHelper.findUserById(projectDTO.projectOwner().userId());
        var project = new Project(projectDTO.projectId(), projectDTO.projectName(), owner.get());
        var participants = projectDTO.projectParticipants().stream().map(pp -> toProjectParticipant(pp, project)).collect(Collectors.toSet());
        project.setProjectParticipants(participants);
        project.setProjectStatus(projectDTO.projectStatus());
        project.setAdminOperationStatus(AdminOperationStatus.valueOf(projectDTO.adminOperationStatus().name()));
        m_projectRepository.save(project);
    }

    private void softDeleteProject(UUID projectId)
    {
        var project = m_serviceHelper.findProjectById(projectId);

        if (project.isPresent())
        {
            project.get().setDeletedAt(LocalDateTime.now());
            m_serviceHelper.saveProject(project.get());
        }
    }
}
