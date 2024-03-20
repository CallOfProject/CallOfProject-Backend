package callofproject.dev.task.service;

import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.task.DatabaseCleaner;
import callofproject.dev.task.Injection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.task.TestUtil.*;
import static callofproject.dev.task.util.BeanName.*;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EntityScan(basePackages = REPOSITORY_BEAN_NAME)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
class TaskServiceApplicationTests
{
    @Autowired
    private Injection m_injection;

    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    User owner;
    User participant1;
    User participant2;
    Project project;
    UUID projectId;

    @BeforeEach
    public void setUp()
    {
        projectId = UUID.randomUUID();
        owner = m_injection.getTaskServiceHelper().saveUser(m_injection.getUserMapper().toUser(provideUserKafkaDTO("owner", "ao@gmail.com")));
        participant1 = m_injection.getTaskServiceHelper().saveUser(m_injection.getUserMapper().toUser(provideUserKafkaDTO("p1", "a@gmail.com")));
        participant2 = m_injection.getTaskServiceHelper().saveUser(m_injection.getUserMapper().toUser(provideUserKafkaDTO("p1", "b@gmail.com")));
        project = m_injection.getTaskServiceHelper().saveProject(provideProject(projectId, "project-1", owner));

        var participantObj1 = provideProjectParticipant(UUID.randomUUID(), project, participant1);
        project.setProjectParticipants(new HashSet<>());
        m_injection.getTaskServiceHelper().saveProject(project);
        project.getProjectParticipants().add(participantObj1);
        m_injection.getTaskServiceHelper().saveProject(project);
        var participantObj2 = provideProjectParticipant(UUID.randomUUID(), project, participant2);
        project.getProjectParticipants().add(participantObj2);
        m_injection.getTaskServiceHelper().saveProject(project);
    }

    @Test
    public void createTask_withGivenValidDTO_shouldReturnCreatedTask()
    {
        var userIds = List.of(participant1.getUserId(), participant2.getUserId());
        var createDTO = provideCreateTaskDTO(project.getProjectId(), "Task-1", "Task-1 description", userIds, Priority.HIGH, TaskStatus.IN_PROGRESS, now(), now().plusDays(5));
        var task = m_injection.getTaskServiceCallback().createTaskCallback(createDTO);

        assertNotNull(task);
        assertEquals(Status.CREATED, task.getStatusCode());
    }

 /*   @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }*/

}
