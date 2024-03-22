package callofproject.dev.task.service;

import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.ProjectParticipant;
import callofproject.dev.data.task.entity.Task;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.data.task.entity.enums.EProjectStatus;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.task.Injection;
import callofproject.dev.task.dto.request.TaskFilterDTO;
import callofproject.dev.task.dto.response.TaskDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static callofproject.dev.task.TestUtil.provideCreateTaskDTO;
import static callofproject.dev.task.util.BeanName.*;
import static java.time.LocalDate.now;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@EntityScan(basePackages = REPOSITORY_BEAN_NAME)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
@Transactional
@SuppressWarnings("all")
public class TaskServiceReadTests
{
    @Autowired
    private Injection m_injection;
    private Task m_task1;
    private Task m_task2;
    private User m_owner;
    private Project m_project;
    private User m_participant1;
    private User m_participant2;
    private User m_participant3;

    @BeforeEach
    void setUp()
    {
        m_owner = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "owner", "owner@gmail.com",
                "owner", "owner", "owner", null));

        m_participant1 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant1", "participant1@gmail.com",
                "participant1", "participant1", "participant1", null));

        m_participant2 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant2", "participant2@gmail.com",
                "participant2", "participant2", "participant2", null));

        m_participant3 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant3", "participant3@gmail.com",
                "participant3", "participant3", "participant3", null));

        m_project = m_injection.getTaskServiceHelper().saveProject(new Project(UUID.randomUUID(), "Test Project", m_owner));

        m_project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), m_project, m_participant1, LocalDateTime.now()));
        m_project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), m_project, m_participant2, LocalDateTime.now()));
        m_project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), m_project, m_participant3, LocalDateTime.now()));

        m_project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        m_project.setAdminOperationStatus(AdminOperationStatus.ACTIVE);

        m_project = m_injection.getTaskServiceHelper().saveProject(m_project);

        var createDTO1 = provideCreateTaskDTO(m_project.getProjectId(), "Task-1",
                "Task-1", List.of(m_participant1.getUserId(), m_participant2.getUserId()), Priority.LOW, TaskStatus.IN_PROGRESS,
                now(), now().plusDays(5));

        var createDTO2 = provideCreateTaskDTO(m_project.getProjectId(), "Task-2",
                "Task-2 Description", List.of(m_participant3.getUserId(), m_participant1.getUserId(), m_participant2.getUserId()), Priority.HIGH, TaskStatus.COMPLETED,
                now(), now().plusDays(5));


        m_task1 = (Task) m_injection.getTaskServiceCallback().createTaskCallback(createDTO1).getObject();
        m_task2 = (Task) m_injection.getTaskServiceCallback().createTaskCallback(createDTO2).getObject();
    }

    @Test
    void testFindAllAssigneesByTaskId_withGivenValidTaskId_shouldReturnAssignees()
    {
        var assignees = m_injection.getTaskServiceCallback().findAllAssigneesByTaskIdCallback(m_owner.getUserId(), m_task1.getTaskId());
        assertEquals(2, assignees.size());
        // reassign for task-2
        assignees = m_injection.getTaskServiceCallback().findAllAssigneesByTaskIdCallback(m_owner.getUserId(), m_task2.getTaskId());
        assertEquals(3, assignees.size());
    }

    @Test
    void testFindAllAssigneesByTaskId_withGivenInvalidUserIdAndValidTaskId_shouldReturnEmptyList()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().findAllAssigneesByTaskIdCallback(UUID.randomUUID(), m_task1.getTaskId()));
    }

    @Test
    void testFindTasksByProjectId_withGivenValidInformation_shouldReturnTasks()
    {
        var tasks = m_injection.getTaskServiceCallback().findTasksByProjectIdCallback(m_project.getProjectId(), 1);
        assertEquals(2, tasks.getItemCount());
    }

    @Test
    void testFindTasksByProjectIdAndUserId_withGivenInformation_shouldReturnTasks()
    {
        // owner of the project (expect 0 tasks)
        var tasks = m_injection.getTaskServiceCallback().findTasksByProjectIdAndUserIdCallback(m_project.getProjectId(), m_owner.getUserId(), 1);
        assertEquals(0, tasks.getItemCount());
        // participant two tasks (participant 1)
        tasks = m_injection.getTaskServiceCallback().findTasksByProjectIdAndUserIdCallback(m_project.getProjectId(), m_participant1.getUserId(), 1);
        assertEquals(2, tasks.getItemCount());
        // Participant one task (participant 3)
        tasks = m_injection.getTaskServiceCallback().findTasksByProjectIdAndUserIdCallback(m_project.getProjectId(), m_participant3.getUserId(), 1);
        assertEquals(1, tasks.getItemCount());
    }

    @Test
    void testFindTaskByFilter_withGivenValidInformation_shouldReturnTasks()
    {
        // find by priority
        var dto = new TaskFilterDTO(Priority.HIGH, null, null, null, m_project.getProjectId(), m_owner.getUserId());
        var tasks = m_injection.getTaskServiceCallback().findTaskByFilterCallback(dto, 1);
        assertEquals(1, tasks.getItemCount());
        // find by status
        dto = new TaskFilterDTO(null, TaskStatus.COMPLETED, null, null, m_project.getProjectId(), m_owner.getUserId());
        tasks = m_injection.getTaskServiceCallback().findTaskByFilterCallback(dto, 1);
        assertEquals(1, tasks.getItemCount());
        // find by date
        dto = new TaskFilterDTO(null, null, LocalDate.now(), null, m_project.getProjectId(), m_owner.getUserId());
        tasks = m_injection.getTaskServiceCallback().findTaskByFilterCallback(dto, 1);
        assertEquals(2, tasks.getItemCount());
    }

    @Test
    void testFindTaskByIdCallback_withGivenValidTaskId_shouldReturnTask()
    {
        var task = m_injection.getTaskServiceCallback().findTaskByIdCallback(m_task1.getTaskId());
        var taskDTO = (TaskDTO) task.getObject();
        assertEquals(Status.OK, task.getStatusCode());
        assertEquals(m_task1.getTaskId(), taskDTO.taskId());
        assertEquals(m_task1.getTitle(), taskDTO.title());
    }


    @Test
    void testFindTaskByIdCallback_withGivenInValidTaskId_shouldThrowsDataServiceException()
    {
        assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().findTaskByIdCallback(UUID.randomUUID()));
    }
}
