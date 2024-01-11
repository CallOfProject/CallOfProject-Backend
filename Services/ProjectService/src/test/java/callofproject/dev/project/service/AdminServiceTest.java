package callofproject.dev.project.service;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.Role;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.project.DatabaseCleaner;
import callofproject.dev.project.Injection;
import callofproject.dev.project.dto.UserDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.mapper.IUserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

import static callofproject.dev.project.util.Constants.*;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {PROJECT_SERVICE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class AdminServiceTest
{
    @Autowired
    private Injection m_injection;
    @Autowired
    private DatabaseCleaner m_databaseCleaner;
    private transient Project m_project;
    private transient UUID testProjectId;
    private transient User testUser;
    private transient User projectOwner;

    @Autowired
    private IUserMapper m_userMapper;

    @BeforeEach
    public void setUp()
    {

        var userDTO = new UserDTO(randomUUID(), "nuricanozturk", "can@mail.com", "Nuri", "Can", "Ozturk", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), null, 0, 0, 0);
        var testUserDTO = new UserDTO(randomUUID(), "halilcanozturk", "halilcan@mail.com", "Halil", "Can", "Ozturk", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), null, 0, 0, 0);

        projectOwner = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(userDTO));
        testUser = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(testUserDTO));

        var testProject = new Project.Builder()
                .setProjectId(randomUUID())
                .setProjectOwner(projectOwner)
                .setFeedbackTimeRange(EFeedbackTimeRange.ONE_MONTH)
                .setProjectImagePath("path/to/image.jpg")
                .setProjectName("Sample Project")
                .setProjectSummary("This is a sample project summary.")
                .setDescription("This is a sample project description.")
                .setProjectAim("The aim of this project is to...")
                .setApplicationDeadline(LocalDate.of(2023, 12, 31))
                .setExpectedCompletionDate(LocalDate.of(2024, 12, 31))
                .setStartDate(LocalDate.of(2023, 1, 1))
                .setMaxParticipant(10)
                .setProjectAccessType(EProjectAccessType.PUBLIC)
                .setProfessionLevel(EProjectProfessionLevel.Expert)
                .setSector(ESector.IT)
                .setDegree(EDegree.BACHELOR)
                .setProjectLevel(EProjectLevel.INTERMEDIATE)
                .setInterviewType(EInterviewType.TEST)
                .setInviteLink("project-invite-link")
                .setTechnicalRequirements("Technical requirements go here.")
                .setSpecialRequirements("Special requirements go here.")
                .build();

        var project2 = new Project.Builder()
                .setProjectId(randomUUID())
                .setProjectOwner(testUser)
                .setFeedbackTimeRange(EFeedbackTimeRange.ONE_MONTH)
                .setProjectImagePath("path/to/image1.jpg")
                .setProjectName("Sample Project 1")
                .setProjectSummary("This is a sample project summary for Project 1.")
                .setDescription("This is a sample project description for Project 1.")
                .setProjectAim("The aim of this project is to...")
                .setApplicationDeadline(LocalDate.of(2023, 12, 31))
                .setExpectedCompletionDate(LocalDate.of(2024, 12, 31))
                .setStartDate(LocalDate.of(2023, 1, 1))
                .setMaxParticipant(10)
                .setProjectAccessType(EProjectAccessType.PUBLIC)
                .setProfessionLevel(EProjectProfessionLevel.Expert)
                .setSector(ESector.IT)
                .setDegree(EDegree.BACHELOR)
                .setProjectLevel(EProjectLevel.INTERMEDIATE)
                .setInterviewType(EInterviewType.TEST)
                .setInviteLink("project-invite-link-1")
                .setTechnicalRequirements("Technical requirements for Project 1 go here.")
                .setSpecialRequirements("Special requirements for Project 1 go here.")
                .build();


        var project3 = new Project.Builder()
                .setProjectId(UUID.randomUUID())
                .setProjectOwner(projectOwner)
                .setFeedbackTimeRange(EFeedbackTimeRange.ONE_MONTH)
                .setProjectImagePath("path/to/image2.jpg")
                .setProjectName("Sample Project 2")
                .setProjectSummary("This is a sample project summary for Project 2.")
                .setDescription("This is a sample project description for Project 2.")
                .setProjectAim("The aim of this project is to...")
                .setApplicationDeadline(LocalDate.of(2023, 12, 31))
                .setExpectedCompletionDate(LocalDate.of(2024, 12, 31))
                .setStartDate(LocalDate.of(2023, 1, 1))
                .setMaxParticipant(10)
                .setProjectAccessType(EProjectAccessType.PUBLIC)
                .setProfessionLevel(EProjectProfessionLevel.Expert)
                .setSector(ESector.IT)
                .setDegree(EDegree.BACHELOR)
                .setProjectLevel(EProjectLevel.INTERMEDIATE)
                .setInterviewType(EInterviewType.TEST)
                .setInviteLink("project-invite-link-2")
                .setTechnicalRequirements("Technical requirements for Project 2 go here.")
                .setSpecialRequirements("Special requirements for Project 2 go here.")
                .build();

        var project4 = new Project.Builder()
                .setProjectId(randomUUID())
                .setProjectOwner(projectOwner)
                .setFeedbackTimeRange(EFeedbackTimeRange.ONE_MONTH)
                .setProjectImagePath("path/to/image3.jpg")
                .setProjectName("Sample Project 3")
                .setProjectSummary("This is a sample project summary for Project 3.")
                .setDescription("This is a sample project description for Project 3.")
                .setProjectAim("The aim of this project is to...")
                .setApplicationDeadline(LocalDate.of(2023, 12, 31))
                .setExpectedCompletionDate(LocalDate.of(2024, 12, 31))
                .setStartDate(LocalDate.of(2023, 1, 1))
                .setMaxParticipant(10)
                .setProjectAccessType(EProjectAccessType.PUBLIC)
                .setProfessionLevel(EProjectProfessionLevel.Expert)
                .setSector(ESector.IT)
                .setDegree(EDegree.BACHELOR)
                .setProjectLevel(EProjectLevel.INTERMEDIATE)
                .setInterviewType(EInterviewType.TEST)
                .setInviteLink("project-invite-link-3")
                .setTechnicalRequirements("Technical requirements for Project 3 go here.")
                .setSpecialRequirements("Special requirements for Project 3 go here.")
                .build();

        m_injection.getProjectServiceHelper().saveProject(project2);
        m_injection.getProjectServiceHelper().saveProject(project3);
        m_injection.getProjectServiceHelper().saveProject(project4);
        m_project = m_injection.getProjectServiceHelper().saveProject(testProject);
        testProjectId = m_project.getProjectId();
    }

    @Test
    void testCancelProject_withValidProjectId_shouldCancelProject()
    {
        var overviewDTO = m_injection.getAdminService().cancelProject(testProjectId);
        assertNotNull(overviewDTO.getObject());
        var dto = (ProjectOverviewDTO) overviewDTO.getObject();
        assertEquals(testProjectId, UUID.fromString(dto.projectId()));
        assertEquals(EProjectStatus.CANCELED, dto.projectStatus());
    }

    @Test
    void testCancelProject_withInvalidProjectId_shouldThrowDataServiceException()
    {
        var invalidId = randomUUID();
        var exception = assertThrows(DataServiceException.class, () -> m_injection.getAdminService().cancelProject(invalidId));
        var msg = format("Message: Project is canceled! , Cause Message:Message: Project with id: %s is not found! ",
                invalidId);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testFindAllProjects_withGivenPageNumber_shouldReturnNotNull()
    {
        var projects = m_injection.getAdminService().findAll(1);
        assertNotNull(projects.getObject());
        assertEquals(4, projects.getItemCount());
    }

    @Test
    void testFindAllProjects_withGivenPageNumber_shouldReturnEmptyList()
    {
        var projects = m_injection.getAdminService().findAll(2);
        assertNull(projects.getObject());
        assertEquals(0, projects.getItemCount());
    }

    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
