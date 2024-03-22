package callofproject.dev.task.service;

import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.ProjectParticipant;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.data.task.entity.enums.EProjectStatus;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.task.Injection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.task.TestUtil.provideCreateTaskDTO;
import static callofproject.dev.task.util.BeanName.*;
import static java.time.LocalDate.now;

@SpringBootTest
@EntityScan(basePackages = REPOSITORY_BEAN_NAME)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
@Transactional
@SuppressWarnings("all")
class TaskServiceCreateTest
{
    @Autowired
    private Injection m_injection;

    @BeforeEach
    public void setUp()
    {
        var owner = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "owner", "owner@gmail.com",
                "owner", "owner", "owner", null));

        var participant1 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant1", "participant1@gmail.com",
                "participant1", "participant1", "participant1", null));

        var participant2 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant2", "participant2@gmail.com",
                "participant2", "participant2", "participant2", null));

        var project = m_injection.getTaskServiceHelper().saveProject(new Project(UUID.randomUUID(), "Test Project", owner));

        project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), project, participant1, LocalDateTime.now()));
        project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), project, participant2, LocalDateTime.now()));

        project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        project.setAdminOperationStatus(AdminOperationStatus.ACTIVE);

        m_injection.getTaskServiceHelper().saveProject(project);
    }


    @Test
    public void testCreateTask_withGivenValidDTO_shouldReturnCreatedTask()
    {
        var p = m_injection.getTaskServiceHelper().findAllProjects().get(0);
        var pp = m_injection.getTaskServiceHelper().findUserByUsername("participant1");

        var createDTO = provideCreateTaskDTO(p.getProjectId(), "Task-1",
                "Task-1", List.of(pp.get().getUserId()), Priority.HIGH, TaskStatus.IN_PROGRESS,
                now(), now().plusDays(5));

        var t = m_injection.getTaskServiceCallback().createTaskCallback(createDTO);

        Assertions.assertNotNull(t);
        Assertions.assertEquals(Status.CREATED, t.getStatusCode());
    }


    @Test
    void testCreateTask_withInvalidUserIds_shouldThrowDataServiceException()
    {
        var p = m_injection.getTaskServiceHelper().findAllProjects().get(0);
        var pp = m_injection.getTaskServiceHelper().findUserByUsername("participant1");

        var createDTO = provideCreateTaskDTO(UUID.randomUUID(), "Task-1",
                "Task-1", List.of(UUID.randomUUID()), Priority.HIGH, TaskStatus.IN_PROGRESS,
                now().plusDays(5), now());

        Assertions.assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().createTaskCallback(createDTO));
    }

    @Test
    void testCreateTask_withInvalidDatesIds_shouldThrowDataServiceException()
    {
        var p = m_injection.getTaskServiceHelper().findAllProjects().get(0);
        var pp = m_injection.getTaskServiceHelper().findUserByUsername("participant1");

        var createDTO = provideCreateTaskDTO(p.getProjectId(), "Task-1",
                "Task-1", List.of(pp.get().getUserId()), Priority.HIGH, TaskStatus.IN_PROGRESS, now().plusDays(5),
                now());

        Assertions.assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().createTaskCallback(createDTO));
    }
}
