package callofproject.dev.project;

import callofproject.dev.data.common.clas.MultipleResponseMessagePageable;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.Project;
import callofproject.dev.data.project.entity.User;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.data.project.repository.IUserRepository;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.dal.TagServiceHelper;
import callofproject.dev.project.config.kafka.KafkaProducer;
import callofproject.dev.project.mapper.IProjectMapper;
import callofproject.dev.project.mapper.IProjectParticipantMapper;
import callofproject.dev.project.service.ProjectService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.project.util.Constants.REPO_PACKAGE;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, REPO_PACKAGE})
@AutoConfigureMockMvc
public class ProjectServiceTest
{
    @Mock
    private ProjectServiceHelper serviceHelper;

    @Mock
    private ProjectTagServiceHelper projectTagServiceHelper;

    @Mock
    private TagServiceHelper tagServiceHelper;

    @Mock
    private KafkaProducer kafkaProducer;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private IProjectMapper projectMapper;

    @Mock
    private IUserRepository m_userRepository;
    @Mock
    private IProjectParticipantMapper projectParticipantMapper;

    @InjectMocks
    private ProjectService projectService;

    // Common test data
    private Project project;
    private User user;
    private UUID projectId;
    private UUID userId;

    @BeforeEach
    void setUp()
    {
        projectId = UUID.randomUUID();
        userId = UUID.randomUUID();
        user = m_userRepository.save(new User(userId, "nuricanozturk", "canozturk309@gmail.com", "Nuri", "Can", "OZTURK"));
        project = new Project.Builder()
                .setProjectId(projectId)
                .setProjectAccessType(EProjectAccessType.PUBLIC)
                .setProjectOwner(user)
                .setProjectLevel(EProjectLevel.INTERMEDIATE)
                .setProfessionLevel(EProjectProfessionLevel.Expert)
                .setSector(ESector.IT)
                .setDegree(EDegree.BACHELOR)
                .setInterviewType(EInterviewType.CODE)
                .setProjectAim("Aim - 2")
                .setDescription("Desc - 2")
                .setStartDate(LocalDate.now())
                .setExpectedCompletionDate(LocalDate.now().plusMonths(9))
                .setApplicationDeadline(LocalDate.now().plusMonths(5))
                .setProjectImagePath("Image - 2")
                .setProjectName("Name - 2")
                .setProjectSummary("Summary - 2")
                .setSpecialRequirements("Special - 2")
                .setTechnicalRequirements("Technical - 2")
                .setMaxParticipant(5)
                .build();
        // Setup additional common data and mocks as needed
    }

    // Test for saveProject
    // Already provided above

    // Test for findAllParticipantProjectByUserId
    @Test
    void testFindAllParticipantProjectByUserId()
    {
        // Arrange
        // Mock serviceHelper and other dependencies as required

        // Act
        MultipleResponseMessagePageable<Object> response = projectService.findAllParticipantProjectByUserId(userId, 1);

        // Assert
        assertNotNull(response);
        // More assertions as needed
    }

    // Test for updateProject
   /* @Test
    void testUpdateProject()
    {
        // Arrange
        ProjectUpdateDTO projectUpdateDTO = new ProjectUpdateDTO();
        // Mock serviceHelper and other dependencies as required

        // Act
        ResponseMessage<Object> response = projectService.updateProject(projectUpdateDTO);

        // Assert
        assertNotNull(response);
        Assertions.assertEquals("Project is updated!", response.getMessage());
        // More assertions as needed
    }*/

    // Test for findAllOwnerProjectsByUserId
    @Test
    void testFindAllOwnerProjectsByUserId()
    {
        // Arrange
        // Mock serviceHelper and other dependencies as required

        // Act
        MultipleResponseMessagePageable<Object> response = projectService.findAllOwnerProjectsByUserId(userId, 1);

        // Assert
        assertNotNull(response);
        // More assertions as needed
    }

    // Test for findAllOwnerProjectsByUsername
    @Test
    void testFindAllOwnerProjectsByUsername()
    {
        // Arrange
        String username = "testUser";
        // Mock serviceHelper and other dependencies as required

        // Act
        MultipleResponseMessagePageable<Object> response = projectService.findAllOwnerProjectsByUsername(username, 1);

        // Assert
        assertNotNull(response);
        // More assertions as needed
    }

    // Test for findProjectOverview
    // Already provided above

    // Test for addProjectJoinRequest
    @Test
    void testAddProjectJoinRequest()
    {
        // Arrange
        // Mock serviceHelper and other dependencies as required

        // Act
        ResponseMessage<Object> response = projectService.addProjectJoinRequest(projectId, userId);

        // Assert
        assertNotNull(response);
        // More assertions as needed
    }

    // Continue writing tests for other methods in a similar fashion...

}
