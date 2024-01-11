package callofproject.dev.project.service;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.common.status.Status;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.ProjectParticipant;
import callofproject.dev.data.project.entity.Role;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.project.DatabaseCleaner;
import callofproject.dev.project.Injection;
import callofproject.dev.project.dto.ParticipantRequestDTO;
import callofproject.dev.project.dto.ParticipantStatusDTO;
import callofproject.dev.project.dto.SaveProjectParticipantDTO;
import callofproject.dev.project.dto.UserDTO;
import callofproject.dev.project.dto.detail.ProjectDetailDTO;
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

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;
import static callofproject.dev.project.util.Constants.TEST_PROPERTIES_FILE;
import static java.lang.String.format;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

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

    private transient Project m_project;
    private transient UUID testProjectId;
    private User testUser;
    private transient User projectOwner;
    @Autowired
    private IUserMapper m_userMapper;

    @BeforeEach
    public void setUp()
    {
        var userDTO = new UserDTO(randomUUID(), "nuricanozturk", "can@mail.com", "Nuri", "Can", "Ozturk", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), 0, 0, 0);
        var testUserDTO = new UserDTO(randomUUID(), "halilcanozturk", "halilcan@mail.com", "Halil", "Can", "Ozturk", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), 0, 0, 0);

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

        m_project = m_injection.getProjectServiceHelper().saveProject(testProject);
        testProjectId = m_project.getProjectId();
    }

    @Test
    public void testAddParticipant_withGivenSaveProjectParticipantDTO_shouldBeTrue()
    {
        // Add a participant to the project
        var participantResult = m_injection.getProjectOwnerService()
                .addParticipant(new SaveProjectParticipantDTO(testUser.getUserId(), testProjectId));
        assertTrue(participantResult.getObject());
        var updatedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertTrue(updatedProject.isPresent());
        assertTrue(updatedProject.get().getProjectParticipants().stream().map(ProjectParticipant::getUser)
                .anyMatch(user -> user.getUserId().equals(testUser.getUserId())));
    }

    @Test
    public void testAddParticipant_withGivenSaveProjectParticipantDTO_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class, () -> m_injection.getProjectOwnerService()
                .addParticipant(new SaveProjectParticipantDTO(randomUUID(), testProjectId)));
        assertEquals("Message: ProjectService::addParticipant ", exception.getMessage());
    }

    @Test
    public void testRemoveParticipant_withGivenProjectIdAndUserId_shouldBeTrue()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        var participantResult = m_injection.getProjectOwnerService()
                .addParticipant(new SaveProjectParticipantDTO(testUser.getUserId(), project.get().getProjectId()));

        assertTrue(participantResult.getObject());

        var removeResult = m_injection
                .getProjectOwnerService()
                .removeParticipantCallback(project.get().getProjectId(), testUser.getUserId());

        var result = (Boolean) removeResult.getObject();
        assertTrue(result);

        assertTrue(project.get().getProjectParticipants().isEmpty());
    }


    @Test
    public void testRemoveParticipant_withGivenProjectIdAndUserId_shouldThrowDataServiceException()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        var participantResult = m_injection.getProjectOwnerService()
                .addParticipant(new SaveProjectParticipantDTO(testUser.getUserId(), project.get().getProjectId()));

        assertTrue(participantResult.getObject());

        var invalidId = randomUUID();
        var removeResult = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectOwnerService().removeParticipantCallback(invalidId, testUser.getUserId()));

        assertEquals(format("Message: Project with id: %s is not found! ", invalidId), removeResult.getMessage());
    }

    @Test
    public void testApproveParticipantRequest_withGivenRequestId_shouldEqual()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        // send join request
        var projectRequest = m_injection.getProjectService()
                .addProjectJoinRequestCallback(project.get().getProjectId(), testUser.getUserId());
        assertEquals(Status.OK, projectRequest.getStatusCode());
        assertNotNull(projectRequest.getObject());

        // verify participant request is created
        project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertFalse(project.get().getProjectParticipantRequests().isEmpty());
        var requestId = project.get().getProjectParticipantRequests().stream().toList().get(0).getParticipantRequestId();
        assertNotNull(requestId);

        // approve join request
        var result = m_injection.getProjectOwnerService().approveParticipantRequestCallback(new ParticipantRequestDTO(requestId, null, true));
        assertTrue(result.getObject() instanceof ParticipantStatusDTO dto && dto.isAccepted());

        // Verify the request is approved
        var p = ((ParticipantStatusDTO) result.getObject()).project();
        assertTrue(p.getProjectParticipantRequests().isEmpty());
        assertEquals(1, p.getProjectParticipants().size());
    }


    @Test
    public void testRejectParticipantRequest_withGivenRequestId_shouldTrue()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        // send join request
        var projectRequest = m_injection.getProjectService()
                .addProjectJoinRequestCallback(project.get().getProjectId(), testUser.getUserId());
        assertEquals(Status.OK, projectRequest.getStatusCode());
        assertNotNull(projectRequest.getObject());

        // verify participant request is created
        project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertFalse(project.get().getProjectParticipantRequests().isEmpty());
        var requestId = project.get().getProjectParticipantRequests().stream().toList().get(0).getParticipantRequestId();
        assertNotNull(requestId);

        // approve join request
        var result = m_injection.getProjectOwnerService().approveParticipantRequestCallback(new ParticipantRequestDTO(requestId, null, false));
        assertFalse(result.getObject() instanceof ParticipantStatusDTO dto && dto.isAccepted());

        // Verify the request is approved
        var p = ((ParticipantStatusDTO) result.getObject()).project();
        assertTrue(p.getProjectParticipantRequests().isEmpty());
        assertTrue(p.getProjectParticipants().isEmpty());
    }


    @Test
    public void testApproveParticipantRequest_withGivenRequestId_shouldThrowDataServiceException()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        // send join request
        var projectRequest = m_injection.getProjectService()
                .addProjectJoinRequestCallback(project.get().getProjectId(), testUser.getUserId());
        assertEquals(Status.OK, projectRequest.getStatusCode());
        assertNotNull(projectRequest.getObject());

        // verify participant request is created
        project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertFalse(project.get().getProjectParticipantRequests().isEmpty());
        var requestId = project.get().getProjectParticipantRequests().stream().toList().get(0).getParticipantRequestId();
        assertNotNull(requestId);

        // approve join request
        var participantRequestDTO = new ParticipantRequestDTO(randomUUID(), null, true);
        var result = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectOwnerService().approveParticipantRequestCallback(participantRequestDTO));

        assertEquals("Message: Participant request is not found! ", result.getMessage());
    }

    @Test
    public void testFinishProject_withGivenValidOwner_shouldEqual()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);
        var result = m_injection.getProjectOwnerService().finishProject(projectOwner.getUserId(), project.get().getProjectId());
        assertNotNull(result.getObject());
        assertInstanceOf(ProjectDetailDTO.class, result.getObject());
        var finishedProject = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertEquals(EProjectStatus.NOT_STARTED, finishedProject.get().getProjectStatus());
    }

    @Test
    public void testFinishProject_withGivenInvalidOwner_shouldThrowDataServiceException()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectOwnerService().finishProject(testUser.getUserId(), project.get().getProjectId()));

        assertEquals("Message: ProjectService::finishProject , Cause Message:Message: You are not owner of this project! ",
                exception.getMessage());
    }

    @Test
    public void testChangeProjectStatus_withGivenValidOwnerAndProjectId_shouldBeEqual()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        var result = m_injection.getProjectOwnerService().changeProjectStatus(projectOwner.getUserId(), project.get().getProjectId(), EProjectStatus.CANCELED);

        assertEquals(EProjectStatus.CANCELED, ((ProjectDetailDTO) result.getObject()).getProjectStatus());
    }


    @Test
    public void testChangeProjectStatus_withGivenValidOwnerAndProjectId_shouldThrowDataServiceException()
    {
        var project = m_injection.getProjectServiceHelper().findProjectById(testProjectId);
        assertNotNull(project);

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectOwnerService()
                        .changeProjectStatus(testUser.getUserId(), project.get().getProjectId(), EProjectStatus.CANCELED));

        assertEquals("Message: ProjectService::changeProjectStatus , Cause Message:Message: You are not owner of this project! ",
                exception.getMessage());
    }


    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}
