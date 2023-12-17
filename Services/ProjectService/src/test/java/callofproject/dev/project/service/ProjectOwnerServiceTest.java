package callofproject.dev.project.service;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.ProjectParticipantRequest;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.project.DatabaseCleaner;
import callofproject.dev.project.Injection;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.dto.UserDTO;
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
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;
import static callofproject.dev.project.util.Constants.TEST_PROPERTIES_FILE;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EntityScan(basePackages = BASE_PACKAGE_BEAN_NAME)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class ProjectOwnerServiceTest
{
    @Autowired
    private Injection m_injection;
    @Autowired
    private DatabaseCleaner m_databaseCleaner;

    private Project m_project;
    private UUID testProjectId;
    private User testUser;
    private User projectOwner;
    @Autowired
    private IUserMapper m_userMapper;

    @BeforeEach
    public void setUp()
    {
        var userDTO = new UserDTO(UUID.randomUUID(), "nuricanozturk", "can@mail.com", "Nuri", "Can", "Ozturk", EOperation.CREATE, 0, 0, 0);
        var testUserDTO = new UserDTO(UUID.randomUUID(), "halilcanozturk", "halilcan@mail.com", "Halil", "Can", "Ozturk", EOperation.CREATE, 0, 0, 0);
        projectOwner = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(userDTO));
        testUser = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(testUserDTO));


        // Create a test project
        Project testProject = new Project.Builder()
                .setProjectId(UUID.randomUUID())
                .setProjectOwner(projectOwner)  // Replace with an actual User object
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
        m_project = m_injection.getProjectServiceHelper().saveProject(testProject);
        testProjectId = m_project.getProjectId();
    }

    @Test
    public void testAddParticipant()
    {
        assertTrue(m_injection.getProjectOwnerService().addParticipant(new SaveProjectParticipantDTO(testUser.getUserId(), testProjectId)));
        var updatedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertTrue(updatedProject.isPresent());
        assertTrue(updatedProject.get().getProjectParticipants().stream().map(ProjectParticipant::getUser).anyMatch(user -> user.getUserId().equals(testUser.getUserId())));

    }

    @Test
    public void testRemoveParticipant()
    {
        assertTrue(m_injection.getProjectOwnerService().addParticipant(new SaveProjectParticipantDTO(testUser.getUserId(), testProjectId)));
        m_injection.getProjectOwnerService().removeParticipant(testProjectId, testUser.getUserId());
        var updatedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertTrue(updatedProject.isPresent());
        assertTrue(updatedProject.get().getProjectParticipants().stream().map(ProjectParticipant::getUser).noneMatch(user -> user.getUserId().equals(testUser.getUserId())));
    }

    @Test
    public void testApproveParticipantRequest()
    {
        assertTrue(m_injection.getProjectOwnerService().addParticipant(new SaveProjectParticipantDTO(testUser.getUserId(), testProjectId)));


        var request = new ProjectParticipantRequest(m_project, testUser);
        var requestId = new ParticipantRequestDTO(request.getParticipantRequestId(), true);
        // Approve the participant request
        m_injection.getProjectOwnerService().approveParticipantRequest(requestId);

        // Verify the request is approved
        var updatedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertTrue(updatedProject.getApprovedRequests().contains(requestId));
    }

    @Test
    public void testFinishProject()
    {
        m_injection.getProjectOwnerService().finishProject(testUserId, testProjectId);
        var finishedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertEquals(EProjectStatus.FINISHED, finishedProject.getStatus());
    }

    @Test
    public void testChangeProjectStatus()
    {
        m_injection.getProjectOwnerService().changeProjectStatus(testUserId, testProjectId, EProjectStatus.IN_PROGRESS);
        var updatedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertEquals(EProjectStatus.IN_PROGRESS, updatedProject.getStatus());
    }

    // Utility method to simulate the creation of a participant request
    private UUID createParticipantRequest(UUID userId, UUID projectId)
    {
        var request = new ProjectParticipantRequest(m_project, testUser);
        // Simulate the creation of a participant request and return its ID
        // This method will depend on how your application handles participant requests
        return UUID.randomUUID(); // Replace with actual logic
    }


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
