package callofproject.dev.task.config.kafka;

import callofproject.dev.data.task.dal.TaskServiceHelper;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.ProjectParticipant;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.task.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.task.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.task.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.task.mapper.IUserMapper;
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
    @KafkaListener(topics = "${spring.kafka.user-topic-name}", groupId = "${spring.kafka.consumer.user-group-id}", containerFactory = "configUserKafkaListener")
    public void listenAuthenticationTopic(UserKafkaDTO dto)
    {
        switch (dto.operation())
        {
            case CREATE, UPDATE -> m_serviceHelper.saveUser(m_userMapper.toUser(dto));
            case DELETE -> softDeleteUser(dto);
            case REGISTER_NOT_VERIFY -> removeUser(dto.userId());
        }
    }


    @KafkaListener(topics = "${spring.kafka.project-info-topic-name}", groupId = "${spring.kafka.consumer.project-info-group-id}", containerFactory = "configProjectInfoKafkaListener")
    public void consumeProjectInfo(ProjectInfoKafkaDTO projectDTO)
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


    //---------------------------------------------------PRIVATE----------------------------------------------------
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
        m_serviceHelper.saveUser(user.get());
    }

    private void removeParticipant(Project project, User user)
    {
        var participant = m_serviceHelper.findProjectParticipantByProjectIdAndUserId(project.getProjectId(), user.getUserId());

        if (participant.isEmpty())
            return;

        project.getProjectParticipants().remove(participant.get());
        user.getProjectParticipants().remove(participant.get());
        m_serviceHelper.removeProjectParticipant(participant.get());
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

    private void removeTask(UUID taskId)
    {
        var task = m_serviceHelper.findTaskById(taskId);

        if (task.isEmpty())
            return;

        var taskAssignees = task.get().getAssignees();

        if (taskAssignees != null && !taskAssignees.isEmpty())
            taskAssignees.forEach(user -> user.getAssignedTasks().remove(task.get()));

        m_serviceHelper.deleteTask(task.get());
    }

    private void removeProject(UUID projectId)
    {
        var project = m_serviceHelper.findProjectById(projectId);

        if (project.isEmpty())
            return;

        project.get().getProjectParticipants().forEach(pp -> removeParticipant(project.get(), pp.getUser()));

        if (!project.get().getTasks().isEmpty())
            project.get().getTasks().stream().map(Task::getTaskId).forEach(this::removeTask);

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
        m_serviceHelper.saveProject(project);
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
