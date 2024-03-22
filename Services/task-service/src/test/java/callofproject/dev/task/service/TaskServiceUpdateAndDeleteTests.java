package callofproject.dev.task.service;

import callofproject.dev.data.common.dsa.Pair;
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
import callofproject.dev.task.dto.request.UpdateTaskDTO;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPOSITORY_BEAN_NAME)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
@Transactional
@SuppressWarnings("all")
public class TaskServiceUpdateAndDeleteTests
{
    @Autowired
    private Injection m_injection;
    private Task m_task;
    private User m_owner;

    @BeforeEach
    void setUp()
    {
        m_owner = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "owner", "owner@gmail.com",
                "owner", "owner", "owner", null));

        var participant1 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant1", "participant1@gmail.com",
                "participant1", "participant1", "participant1", null));

        var participant2 = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "participant2", "participant2@gmail.com",
                "participant2", "participant2", "participant2", null));

        var project = m_injection.getTaskServiceHelper().saveProject(new Project(UUID.randomUUID(), "Test Project", m_owner));

        project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), project, participant1, LocalDateTime.now()));
        project.addProjectParticipant(new ProjectParticipant(UUID.randomUUID(), project, participant2, LocalDateTime.now()));

        project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        project.setAdminOperationStatus(AdminOperationStatus.ACTIVE);

        project = m_injection.getTaskServiceHelper().saveProject(project);

        var pp = m_injection.getTaskServiceHelper().findUserByUsername("participant1");

        var createDTO = provideCreateTaskDTO(project.getProjectId(), "Task-1",
                "Task-1", List.of(pp.get().getUserId()), Priority.HIGH, TaskStatus.IN_PROGRESS,
                now(), now().plusDays(5));

        m_task = (Task) m_injection.getTaskServiceCallback().createTaskCallback(createDTO).getObject();
    }

    @Test
    void testUpdateTask_withGivenValidInformation_shouldReturnUpdatedTask()
    {
        // Arrange
        var userIds = m_task.getAssignees().stream().map(User::getUserId).toList();
        var updateTaskDTO = new UpdateTaskDTO(m_task.getTaskId(), m_owner.getUserId(), "Updated-Task", "Updated-Description",
                Priority.CRITICAL, TaskStatus.IN_PROGRESS, userIds, LocalDate.now(), LocalDate.now().plusMonths(4));

        // Act
        var updatedTaskResponseObj = m_injection.getTaskServiceCallback().updateTaskCallback(updateTaskDTO);
        var updatedTaskPair = (Pair<Task, TaskDTO>) updatedTaskResponseObj.getObject();
        var updatedTaskDTO = updatedTaskPair.getFirst();

        // Assert
        assertEquals(Status.OK, updatedTaskResponseObj.getStatusCode());
        assertEquals(TaskStatus.IN_PROGRESS, updateTaskDTO.taskStatus());
        assertEquals("Updated-Task", updatedTaskDTO.getTitle());
        assertEquals("Updated-Description", updatedTaskDTO.getDescription());
        assertEquals(Priority.CRITICAL, updatedTaskDTO.getPriority());
    }


    @Test
    void testUpdateTask_withGivenInvalidInformation_shouldThrowsDataServiceException()
    {
        var userIds = m_task.getAssignees().stream().map(User::getUserId).toList();

        var updateTaskDTO = new UpdateTaskDTO(m_task.getTaskId(), UUID.randomUUID(), "Updated-Task", "Updated-Description",
                Priority.CRITICAL, TaskStatus.IN_PROGRESS, userIds, LocalDate.now().plusMonths(4), LocalDate.now());

        assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().updateTaskCallback(updateTaskDTO));
    }

    @Test
    void testDeleteTask_withGivenValidInformation_shouldReturnDeletedTask()
    {
        // Arrange
        var taskId = m_task.getTaskId();

        // Act
        var deleteTaskResponseObj = m_injection.getTaskServiceCallback().deleteTaskCallback(taskId);

        // Assert
        assertEquals(Status.OK, deleteTaskResponseObj.getStatusCode());
        assertTrue(m_injection.getTaskRepository().findAll().isEmpty());
    }

    @Test
    void testDeleteTask_withGivenInvalidInformation_shouldThrowsDataServiceException()
    {
        var taskId = UUID.randomUUID();

        assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().deleteTaskCallback(taskId));
    }
}
