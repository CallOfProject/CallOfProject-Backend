package callofproject.dev.service.interview.config.kafka;

import callofproject.dev.data.interview.dal.InterviewServiceHelper;
import callofproject.dev.data.interview.entity.Project;
import callofproject.dev.data.interview.entity.ProjectParticipant;
import callofproject.dev.data.interview.entity.User;
import callofproject.dev.data.interview.entity.enums.AdminOperationStatus;
import callofproject.dev.service.interview.config.kafka.dto.ProjectInfoKafkaDTO;
import callofproject.dev.service.interview.config.kafka.dto.ProjectParticipantKafkaDTO;
import callofproject.dev.service.interview.config.kafka.dto.UserKafkaDTO;
import callofproject.dev.service.interview.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

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


    @KafkaListener(topics = "${spring.kafka.user-topic-name}", groupId = "${spring.kafka.consumer.user-group-id}", containerFactory = "configUserKafkaListener")
    public void consumeAuthenticationTopic(UserKafkaDTO dto)
    {
        m_serviceHelper.saveUser(m_userMapper.toUser(dto));
    }


    @KafkaListener(topics = "${spring.kafka.project-info-topic-name}", groupId = "${spring.kafka.consumer.project-info-group-id}", containerFactory = "configProjectInfoKafkaListener")
    public void consumeProjectInfo(ProjectInfoKafkaDTO projectDTO)
    {
        var owner = m_serviceHelper.findUserById(projectDTO.projectOwner().userId());
        var project = new Project(projectDTO.projectId(), projectDTO.projectName(), owner.get());
        var participants = projectDTO.projectParticipants().stream().map(pp -> toProjectParticipant(pp, project)).collect(Collectors.toSet());

        project.setProjectParticipants(participants);
        project.setProjectStatus(projectDTO.projectStatus());
        project.setAdminOperationStatus(AdminOperationStatus.valueOf(projectDTO.adminOperationStatus().name()));
        m_serviceHelper.createProject(project);
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

    private void removeParticipant(Project project, User user)
    {
        var participant = m_serviceHelper.findProjectParticipantByUserIdAndProjectId(user.getUserId(), project.getProjectId());

        if (participant.isEmpty())
            return;

        project.getProjectParticipants().remove(participant.get());
        user.getProjectParticipants().remove(participant.get());
        m_serviceHelper.removeProjectParticipant(participant.get());
    }

    private void addParticipant(ProjectParticipantKafkaDTO dto, Project project, User user)
    {
        var participant = new ProjectParticipant(dto.projectParticipantId(), project, user, dto.joinDate());
        m_serviceHelper.createProjectParticipant(participant);
    }

    private ProjectParticipant toProjectParticipant(ProjectParticipantKafkaDTO participant, Project project)
    {
        return new ProjectParticipant(project, m_serviceHelper.findUserById(participant.userId()).get());
    }
}
