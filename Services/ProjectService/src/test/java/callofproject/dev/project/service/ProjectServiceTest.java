package callofproject.dev.project.service;

import callofproject.dev.data.common.enums.EOperation;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.Role;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.project.DatabaseCleaner;
import callofproject.dev.project.Injection;
import callofproject.dev.project.dto.ProjectSaveDTO;
import callofproject.dev.project.dto.ProjectUpdateDTO;
import callofproject.dev.project.dto.UserDTO;
import callofproject.dev.project.dto.detail.ProjectDetailDTO;
import callofproject.dev.project.dto.detail.ProjectsDetailDTO;
import callofproject.dev.project.dto.discovery.ProjectsDiscoveryDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewDTO;
import callofproject.dev.project.dto.overview.ProjectOverviewsDTO;
import callofproject.dev.project.dto.owner.ProjectOwnerViewDTO;
import callofproject.dev.project.mapper.IUserMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;
import static callofproject.dev.project.util.Constants.TEST_PROPERTIES_FILE;
import static java.util.UUID.randomUUID;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EntityScan(basePackages = BASE_PACKAGE_BEAN_NAME)
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
public class ProjectServiceTest
{
    @Autowired
    private Injection m_injection;
    @Autowired
    private DatabaseCleaner m_databaseCleaner;
    private Project project_1;
    private Project project_2;
    private Project project_3;
    private Project project_4;
    private User testUser;
    private User testUser2_OwnerProject_2;
    private User projectOwner;
    private UUID project3Id;
    private transient UUID testUserId;
    @Autowired
    private IUserMapper m_userMapper;

    @BeforeEach
    public void setUp()
    {
        testUserId = randomUUID();
        project3Id = randomUUID();

        var userDTO = new UserDTO(randomUUID(), "nuricanozturk", "can@mail.com", "Nuri", "Can", "Ozturk", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), 0, 0, 0);
        var testUserDTO = new UserDTO(testUserId, "halilcanozturk", "halilcan@mail.com", "Halil", "Can", "Ozturk", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), 0, 0, 0);
        var testUser2DTO = new UserDTO(randomUUID(), "emirkafadar", "emir@mail.com", "Emir", "", "Kafadar", EOperation.CREATE,
                "123", Set.of(new Role("ROLE_USER")), 0, 0, 0);

        projectOwner = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(userDTO));
        testUser = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(testUserDTO));
        testUser2_OwnerProject_2 = m_injection.getProjectServiceHelper().addUser(m_userMapper.toUser(testUser2DTO));

        var project1 = new Project.Builder()
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
                .setProjectOwner(testUser2_OwnerProject_2)
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
                .setProjectId(project3Id)
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

        project1.addProjectParticipant(testUser);
        project2.addProjectParticipant(testUser);
        project1.addProjectParticipant(testUser2_OwnerProject_2);

        project_1 = m_injection.getProjectServiceHelper().saveProject(project1);
        project_2 = m_injection.getProjectServiceHelper().saveProject(project2);
        project_3 = m_injection.getProjectServiceHelper().saveProject(project3);
        project_4 = m_injection.getProjectServiceHelper().saveProject(project4);
    }

    @Test
    void testSaveProject_withGivenProjectSaveDTO_shouldNotNull()
    {
        var project = new Project.Builder()
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

        var savedProject = m_injection.getProjectServiceHelper().saveProject(project);
        assertNotNull(savedProject);
        var existsProject = m_injection.getProjectServiceHelper().findProjectById(savedProject.getProjectId());
        assertNotNull(existsProject);
    }


    @Test
    void testSaveProject_withGivenProjectSaveDTO_shouldThrowDataServiceException()
    {
        var invalidUserId = UUID.randomUUID();
        var projectSaveDto = new ProjectSaveDTO(
                invalidUserId,
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "This is a sample project description.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                LocalDate.of(2023, 1, 1),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectAccessType.PUBLIC,
                EProjectProfessionLevel.Expert,
                ESector.IT,
                EDegree.BACHELOR,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                EFeedbackTimeRange.ONE_MONTH,
                List.of("tag1", "tag2")
        );


        var exception = Assertions.assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().saveProject(projectSaveDto));

        var msg = String.format("Message: ProjectService::saveProject , Cause Message:Message: User with id: %s is not found! ",
                invalidUserId);

        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testUpdateProject_withGivenProjectUpdateDTO_shouldNotNull()
    {
        var projectUpdateDto = new ProjectUpdateDTO(
                projectOwner.getUserId(),
                project_1.getProjectId(),
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "This is a sample project description.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectAccessType.PUBLIC,
                EProjectProfessionLevel.Expert,
                ESector.Finance,
                EDegree.MASTER,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                EFeedbackTimeRange.ONE_WEEK,
                LocalDate.of(2023, 1, 1),
                List.of("tag1", "tag2")
        );

        var updatedProject = m_injection.getProjectService().updateProject(projectUpdateDto);
        var detailDto = (ProjectDetailDTO) updatedProject.getObject();
        assertNotNull(updatedProject);
        assertEquals(EFeedbackTimeRange.ONE_WEEK, detailDto.getFeedbackTimeRange());
        assertEquals(EProjectLevel.INTERMEDIATE, detailDto.getProjectLevel());
        assertEquals(ESector.Finance, detailDto.getSector());
    }

    @Test
    void testUpdateProject_withGivenProjectUpdateDTO_shouldThrowDataServiceException()
    {
        var projectUpdateDto = new ProjectUpdateDTO(
                testUser2_OwnerProject_2.getUserId(),
                project_1.getProjectId(),
                "path/to/image.jpg",
                "Sample Project",
                "This is a sample project summary.",
                "This is a sample project description.",
                "The aim of this project is to...",
                LocalDate.of(2023, 12, 31),
                LocalDate.of(2024, 12, 31),
                10,
                "Technical requirements go here.",
                "Special requirements go here.",
                EProjectAccessType.PUBLIC,
                EProjectProfessionLevel.Expert,
                ESector.Finance,
                EDegree.MASTER,
                EProjectLevel.INTERMEDIATE,
                EInterviewType.TEST,
                EFeedbackTimeRange.ONE_WEEK,
                LocalDate.of(2023, 1, 1),
                List.of("tag1", "tag2")
        );

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().updateProject(projectUpdateDto));
        assertEquals("Message: ProjectService::updateProject , Cause Message:Message: You are not owner of this project! ", exception.getMessage());

    }

    @Test
    void testFindAllParticipantProjectByUserId_withGivenUserIdAndPage_shouldNotNullAndEqual()
    {
        var projectList = m_injection.getProjectService().findAllParticipantProjectByUserId(testUser.getUserId(), 1);
        assertNotNull(projectList.getObject());
        assertInstanceOf(ProjectOverviewsDTO.class, projectList.getObject());
        assertEquals(2, projectList.getItemCount());
    }

    @Test
    void testFindAllParticipantProjectByUserId_withGivenInvalidUserIdAndPage_shouldThrowDataServiceException()
    {
        var invalidUserId = UUID.randomUUID();

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().findAllParticipantProjectByUserId(invalidUserId, 1));

        var msg = String.format("Message: ProjectService::findAllParticipantProjectByUserId , Cause Message:Message: User with id: %s is not found! ",
                invalidUserId);

        assertEquals(msg, exception.getMessage());
    }


    @Test
    void testFindAllOwnerProjectsByUserId_withGivenUserIdAndPage_shouldNotNullAndEqual()
    {
        var projectList = m_injection.getProjectService().findAllOwnerProjectsByUserId(projectOwner.getUserId(), 1);
        assertNotNull(projectList.getObject());
        assertInstanceOf(ProjectsDetailDTO.class, projectList.getObject());
        assertEquals(3, projectList.getItemCount());
    }

    @Test
    void testFindAllOwnerProjectsByUserId_withGivenInvalidUserIdAndPage_shouldThrowDataServiceException()
    {
        var invalidUserId = UUID.randomUUID();

        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().findAllOwnerProjectsByUserId(invalidUserId, 1));

        var msg = String.format("Message: ProjectService::findAllOwnerProjectsByUserId , Cause Message:Message: User with id: %s is not found! ",
                invalidUserId);

        assertEquals(msg, exception.getMessage());
    }


    @Test
    void testFindAllOwnerProjectsByUsername_withGivenUserIdAndPage_shouldNotNullAndEqual()
    {
        var projectList = m_injection.getProjectService().findAllOwnerProjectsByUsername(projectOwner.getUsername(), 1);
        assertNotNull(projectList.getObject());
        assertInstanceOf(ProjectsDetailDTO.class, projectList.getObject());
        assertEquals(3, projectList.getItemCount());
    }

    @Test
    void testFindAllOwnerProjectsByUsername_withGivenInvalidUserIdAndPage_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().findAllOwnerProjectsByUsername("mahmut", 1));

        var msg = String.format("Message: ProjectService::findAllOwnerProjectsByUsername , Cause Message:Message: User with id: %s is not found! ",
                "mahmut");

        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testFindProjectOverview_withGivenProjectId_shouldNotNullAndEqual()
    {
        var projectOverview = m_injection.getProjectService().findProjectOverview(project_1.getProjectId());
        assertNotNull(projectOverview.getObject());
        assertInstanceOf(ProjectOverviewDTO.class, projectOverview.getObject());
        var dto = (ProjectOverviewDTO) projectOverview.getObject();
        assertEquals(project_1.getProjectId(), UUID.fromString(dto.projectId()));
        assertEquals(project_1.getProjectName(), dto.projectTitle());
        assertEquals(project_1.getProjectSummary(), dto.projectSummary());
    }


    @Test
    void testFindProjectOverview_withGivenInvalidProjectId_shouldThrowDataServiceException()
    {
        var invalidProjectId = UUID.randomUUID();
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().findProjectOverview(invalidProjectId));
        var msg = String.format("Message: ProjectService::findProjectOverview , Cause Message:Message: Project with id: %s is not found! ", invalidProjectId);
        assertEquals(msg, exception.getMessage());
    }


    @Test
    void testFindProjectDetail_withGivenProjectId_shouldNotNullAndEqual()
    {
        var projectDetail = m_injection.getProjectService().findProjectOwnerView(projectOwner.getUserId(), project_1.getProjectId());
        assertNotNull(projectDetail.getObject());
        assertInstanceOf(ProjectOwnerViewDTO.class, projectDetail.getObject());
        var dto = (ProjectOwnerViewDTO) projectDetail.getObject();
        assertEquals(project_1.getProjectId(), UUID.fromString(dto.getProjectId()));
        assertEquals(project_1.getProjectName(), dto.getProjectTitle());
        assertEquals(project_1.getProjectSummary(), dto.getProjectSummary());
    }

    @Test
    void testFindProjectDetail_withGivenInvalidProjectId_shouldThrowDataServiceException()
    {
        var invalidProjectId = UUID.randomUUID();
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().findProjectOwnerView(projectOwner.getUserId(), invalidProjectId));
        var msg = String.format("Message: ProjectService::findProjectOwnerView , Cause Message:Message: Project with id: %s is not found! ", invalidProjectId);
        assertEquals(msg, exception.getMessage());
    }

    @Test
    void testFindProjectDiscoveryViews_withGivenPageNumber_shouldNotNull()
    {
        var projectDiscoveryViews = m_injection.getProjectService().findAllProjectDiscoveryView(1);
        assertNotNull(projectDiscoveryViews.getObject());
        assertInstanceOf(ProjectsDiscoveryDTO.class, projectDiscoveryViews.getObject());
        assertEquals(4, projectDiscoveryViews.getItemCount());
    }

    @Test
    void testFindProjectDiscoveryViews_withGivenInvalidPageNumber_shouldThrowDataServiceException()
    {
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().findAllProjectDiscoveryView(0));

        assertEquals("Message: ProjectService::findAllProjectDiscoveryView , Cause Message:Page index must not be less than zero",
                exception.getMessage());
    }

    // USER DAKİ MERGE, DETACH KALDIRILDI, PARTICIPANT REQUESTTE DE
    @Test
    void testAddProjectJoinRequest_withGivenProjectIdAndUserId_shouldNotNull()
    {
        var user = m_injection.getProjectServiceHelper().findUserById(testUser.getUserId());
        var project = m_injection.getProjectServiceHelper().findProjectById(project_4.getProjectId());
        System.out.println(user.get().getUserId());
        System.out.println(project.get().getProjectId());
        var projectJoinRequest = m_injection.getProjectService()
                .addProjectJoinRequestCallback(project.get().getProjectId(), user.get().getUserId());
        assertNotNull(projectJoinRequest.getObject());
    }

    @Test
    void testAddProjectJoinRequest_withGivenInvalidProjectIdAndUserId_shouldThrowDataServiceException()
    {
        var invalidProjectId = UUID.randomUUID();
        var exception = assertThrows(DataServiceException.class,
                () -> m_injection.getProjectService().addProjectJoinRequestCallback(invalidProjectId, testUser.getUserId()));
        var msg = String.format("Message: User with id: %s or Project with id: %s is not found! ", testUser.getUserId(), invalidProjectId);
        assertEquals(msg, exception.getMessage());
    }

    @AfterEach
    public void tearDown()
    {
        m_databaseCleaner.clearH2Database();
    }
}