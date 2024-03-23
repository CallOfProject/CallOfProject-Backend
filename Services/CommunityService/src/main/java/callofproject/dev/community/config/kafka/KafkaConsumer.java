package callofproject.dev.community.config.kafka;

import callofproject.dev.community.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.community.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.community.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.community.mapper.IUserMapper;
import callofproject.dev.data.community.dal.CommunityServiceHelper;
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
 * @author Nuri Can ÖZTÜRK
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
        switch (dto.operation())
        {
            case CREATE, UPDATE -> m_userRepository.save(m_userMapper.toUser(dto));
            case DELETE -> removeUser(dto.userId());
            case SOFT_DELETED -> softDeleteUser(dto);
        }
    }

    /**
     * Listens to the specified Kafka topic and processes ProjectInfoKafkaDTO messages.
     *
     * @param projectDTO The ProjectInfoKafkaDTO message received from Kafka.
     */
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


    /**
     * Listens to the specified Kafka topic and processes ProjectParticipantKafkaDTO messages.
     *
     * @param dto The ProjectParticipantKafkaDTO message received from Kafka.
     */
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


    /**
     * Removes a user with the specified ID.
     *
     * @param userId The ID of the user to remove.
     */
    private void removeUser(UUID userId)
    {
        var user = m_serviceHelper.findUserById(userId);
        if (user.isEmpty())
            return;
        m_serviceHelper.deleteUser(user.get());
    }

    /**
     * Soft deletes a user with the specified ID.
     *
     * @param dto The UserKafkaDTO message containing the user ID and deletion timestamp.
     */
    private void softDeleteUser(UserKafkaDTO dto)
    {
        var user = m_serviceHelper.findUserById(dto.userId());
        if (user.isEmpty())
            return;
        user.get().setDeletedAt(dto.deletedAt());
        m_serviceHelper.upsertUser(user.get());
    }

    /**
     * Removes a participant from the specified project.
     *
     * @param project The project to remove the participant from.
     * @param user    The user to remove from the project.
     */
    private void removeParticipant(Project project, User user)
    {
        var participant = m_serviceHelper.findProjectParticipantByProjectIdAndUserId(project.getProjectId(), user.getUserId());

        if (participant.isEmpty())
            return;

        project.getProjectParticipants().remove(participant.get());
        user.getProjectParticipants().remove(participant.get());
        m_serviceHelper.removeParticipant(participant.get());
    }

    /**
     * Adds a participant to the specified project.
     *
     * @param dto     The ProjectParticipantKafkaDTO message containing the project ID, user ID, and join date.
     * @param project The project to add the participant to.
     * @param user    The user to add to the project.
     */
    private void addParticipant(ProjectParticipantKafkaDTO dto, Project project, User user)
    {
        var participant = new ProjectParticipant(dto.projectParticipantId(), project, user, dto.joinDate());
        m_serviceHelper.saveParticipant(participant);
    }

    /**
     * Converts a ProjectParticipantKafkaDTO to a ProjectParticipant.
     *
     * @param participant The ProjectParticipantKafkaDTO to convert.
     * @param project     The project the participant is associated with.
     * @return The converted ProjectParticipant.
     */
    private ProjectParticipant toProjectParticipant(ProjectParticipantKafkaDTO participant, Project project)
    {
        return new ProjectParticipant(project, m_serviceHelper.findUserById(participant.userId()).get());
    }

    /**
     * Removes a project with the specified ID.
     *
     * @param projectId The ID of the project to remove.
     */
    private void removeProject(UUID projectId)
    {
        var project = m_serviceHelper.findProjectById(projectId);

        if (project.isEmpty())
            return;

        project.get().getProjectParticipants().forEach(pp -> removeParticipant(project.get(), pp.getUser()));
        m_serviceHelper.deleteProjectById(projectId);
    }

    /**
     * Creates a project from the specified ProjectInfoKafkaDTO.
     *
     * @param projectDTO The ProjectInfoKafkaDTO to create the project from.
     */
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

    /**
     * Soft deletes a project with the specified ID.
     *
     * @param projectId The ID of the project to soft delete.
     */
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
