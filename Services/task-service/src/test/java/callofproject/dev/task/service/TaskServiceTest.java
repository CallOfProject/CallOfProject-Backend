package callofproject.dev.task.service;

import callofproject.dev.data.task.entity.Project;
import callofproject.dev.data.task.entity.ProjectParticipant;
import callofproject.dev.data.task.entity.User;
import callofproject.dev.data.task.entity.enums.AdminOperationStatus;
import callofproject.dev.data.task.entity.enums.EProjectStatus;
import callofproject.dev.data.task.entity.enums.Priority;
import callofproject.dev.data.task.entity.enums.TaskStatus;
import callofproject.dev.task.DatabaseCleaner;
import callofproject.dev.task.Injection;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

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
class TaskServiceTest
{
    private static final UUID OWNER_UUID = UUID.fromString("e607cdaf-9d2e-4e65-a1e1-96d1d796e537");
    private static final UUID PARTICIPANT1_UUID = UUID.fromString("ee6c5299-78cc-4e58-a277-52b74576d6e3");
    private static final UUID PARTICIPANT2_UUID = UUID.fromString("37b9569f-545e-42e7-8db0-70fc8b6bf0fc");
    private static final UUID PROJECT_UUID = UUID.fromString("b365f17e-94e4-4c4c-a481-41fbd2ff4b5f");
    private static final UUID OBJ_1_UUID = UUID.fromString("b365f17e-94e4-4c4c-a281-45ffd2dd4b1f");
    private static final UUID OBJ_2_UUID = UUID.fromString("b3a5f57d-94e4-4c4c-a481-42fab2cf6b5d");

    @Autowired
    private Injection m_injection;

    @Autowired
    private DatabaseCleaner m_databaseCleaner;


    @BeforeEach
    public void setUp()
    {
        var owner = m_injection.getTaskServiceHelper().saveUser(new User(OWNER_UUID, "owner", "owner@gmail.com",
                "owner", "owner", "owner", null));

        var participant1 = m_injection.getTaskServiceHelper().saveUser(new User(PARTICIPANT1_UUID, "participant1", "participant1@gmail.com",
                "participant1", "participant1", "participant1", null));

        var participant2 = m_injection.getTaskServiceHelper().saveUser(new User(PARTICIPANT2_UUID, "participant2", "participant2@gmail.com",
                "participant2", "participant2", "participant2", null));

        var project = m_injection.getTaskServiceHelper().saveProject(new Project(PROJECT_UUID, "Test Project", owner));

        project.addProjectParticipant(new ProjectParticipant(OBJ_1_UUID, project, participant1, LocalDateTime.now()));
        project.addProjectParticipant(new ProjectParticipant(OBJ_2_UUID, project, participant2, LocalDateTime.now()));

        project.setProjectStatus(EProjectStatus.IN_PROGRESS);
        project.setAdminOperationStatus(AdminOperationStatus.ACTIVE);

        m_injection.getTaskServiceHelper().saveProject(project);
    }


    @Test
    public void createTask_withGivenValidDTO_shouldReturnCreatedTask()
    {
        var p = m_injection.getTaskServiceHelper().findProjectById(PROJECT_UUID);
        var pp = m_injection.getTaskServiceHelper().findUserByUsername("participant1");
        var createDTO = provideCreateTaskDTO(p.get().getProjectId(), "Task-1",
                "Task-1dsadsa", List.of(pp.get().getUserId()), Priority.HIGH, TaskStatus.IN_PROGRESS,
                now(), now().plusDays(5));

        var t = m_injection.getTaskServiceCallback().createTaskCallback(createDTO);

        Assertions.assertNotNull(t);
    }

 /*   @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }*/

}
