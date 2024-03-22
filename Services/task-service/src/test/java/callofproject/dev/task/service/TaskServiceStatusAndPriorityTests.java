package callofproject.dev.task.service;

import callofproject.dev.data.common.dsa.Pair;
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
import callofproject.dev.task.dto.request.ChangeTaskPriorityDTO;
import callofproject.dev.task.dto.request.ChangeTaskStatusDTO;
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
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPOSITORY_BEAN_NAME)
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
@Transactional
public class TaskServiceStatusAndPriorityTests
{
    @Autowired
    private Injection m_injection;

    private Task m_task;
    private User owner;

    @BeforeEach
    void setUp()
    {
        owner = m_injection.getTaskServiceHelper().saveUser(new User(UUID.randomUUID(), "owner", "owner@gmail.com",
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

        project = m_injection.getTaskServiceHelper().saveProject(project);

        var pp = m_injection.getTaskServiceHelper().findUserByUsername("participant1");

        var createDTO = provideCreateTaskDTO(project.getProjectId(), "Task-1",
                "Task-1", List.of(pp.get().getUserId()), Priority.HIGH, TaskStatus.IN_PROGRESS,
                now(), now().plusDays(5));

        m_task = (Task) m_injection.getTaskServiceCallback().createTaskCallback(createDTO).getObject();
    }

    @Test
    void testChangeTaskStatus_withGivenValidInformation_shouldReturnChangedStatus()
    {
        // Arrange
        var beforeUpdatedTaskStatus = m_task.getTaskStatus();
        var changeStatusDTO = new ChangeTaskStatusDTO(m_task.getTaskId(), owner.getUserId(), TaskStatus.CANCELED);

        // Act
        var updatedTask = m_injection.getTaskServiceCallback().changeTaskStatusCallback(changeStatusDTO);

        // Assert
        var afterUpdatedTaskStatus = ((Pair<String, Task>) updatedTask.getObject()).getSecond().getTaskStatus();
        assertEquals(TaskStatus.CANCELED, afterUpdatedTaskStatus);
        assertNotEquals(afterUpdatedTaskStatus, beforeUpdatedTaskStatus);
    }

    @Test
    void testChangeTaskPriority_withGivenValidInformation_shouldReturnChangedPriority()
    {
        // Arrange
        var beforeUpdatedPriority = m_task.getPriority();
        var changePriorityDTO = new ChangeTaskPriorityDTO(m_task.getTaskId(), owner.getUserId(), Priority.LOW);

        // Act
        var updatedTask = m_injection.getTaskServiceCallback().changeTaskPriorityCallback(changePriorityDTO);

        // Assert
        var afterUpdatedPriority = ((Pair<String, Task>) updatedTask.getObject()).getSecond().getPriority();
        assertEquals(Priority.LOW, afterUpdatedPriority);
        assertNotEquals(afterUpdatedPriority, beforeUpdatedPriority);
    }


    @Test
    void testChangeTaskStatus_withGivenInvalidInformation_shouldThrowDataServiceException()
    {
        // Arrange
        var changeStatusDTO = new ChangeTaskStatusDTO(UUID.randomUUID(), owner.getUserId(), TaskStatus.CANCELED);

        // Act & Assert
        assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().changeTaskStatusCallback(changeStatusDTO));
    }

    @Test
    void testChangeTaskPriority_withGivenInvalidInformation_shouldThrowDataServiceException()
    {
        // Arrange
        var changePriorityDTO = new ChangeTaskPriorityDTO(UUID.randomUUID(), owner.getUserId(), Priority.LOW);

        // Act & Assert
        assertThrows(DataServiceException.class, () -> m_injection.getTaskServiceCallback().changeTaskPriorityCallback(changePriorityDTO));
    }
}
