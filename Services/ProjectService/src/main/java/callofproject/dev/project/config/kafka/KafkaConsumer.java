package callofproject.dev.project.config.kafka;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.project.dto.UserDTO;
import callofproject.dev.project.mapper.IUserMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static callofproject.dev.util.stream.StreamUtil.toStreamConcurrent;


@Service
public class KafkaConsumer
{
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
        if (dto.operation() == EOperation.CREATE || dto.operation() == EOperation.UPDATE)
            m_serviceHelper.addUser(m_userMapper.toUser(dto));

        if (dto.operation() == EOperation.DELETE)
        {
            var root = m_serviceHelper.findUserByUsername("cop_root");
            var projects = toStreamConcurrent(m_serviceHelper.findAllProjectByProjectOwnerUserId(dto.userId(), 1)).toList();

            projects.forEach(project -> {
                project.setProjectOwner(root.get());
                m_serviceHelper.saveProject(project);
            });

            var rootProjects = toStreamConcurrent(m_serviceHelper.findAllProjectByProjectOwnerUserId(root.get().getUserId(), 1)).toList();
            rootProjects.stream().map(Project::getProjectName).forEach(System.out::println);

            var others = toStreamConcurrent(m_serviceHelper.findAllProjectParticipantByUserId(dto.userId())).toList();

            others.forEach(projectParticipant -> {
                projectParticipant.setUser(root.get());
                m_serviceHelper.saveProjectParticipant(projectParticipant);
            });

            m_serviceHelper.removeUser(dto.userId());
        }
    }
}
