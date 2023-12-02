package callofproject.dev.project;

import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.data.project.entity.*;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.data.project.repository.*;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.UUID;

import static callofproject.dev.project.util.Constants.*;

@SpringBootTest
@EntityScan(basePackages = REPO_PACKAGE)
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, REPO_PACKAGE})
@TestPropertySource(locations = TEST_PROPERTIES_FILE)
class ProjectServiceApplicationTests
{
    @Autowired
    private ProjectServiceHelper m_serviceHelper;

    @Autowired
    IUserRepository m_userRepository;
    @Autowired
    IProjectRepository m_projectRepository;
    @Autowired
    private IProjectAccessTypeRepository m_accessTypeRepository;
    @Autowired
    private IProjectLevelRepository m_levelRepository;
    @Autowired
    private IProjectProfessionLevelRepository m_professionLevelRepository;
    @Autowired
    private ISectorRepository m_sectorRepository;
    @Autowired
    private IDegreeRepository m_degreeRepository;
    @Autowired
    private IInterviewTypeRepository m_interviewTypeRepository;


    @Test
    @Order(1)
    void contextLoads() throws InterruptedException
    {
        m_accessTypeRepository.save(new ProjectAccessType(EProjectAccessType.PUBLIC));
        m_levelRepository.save(new ProjectLevel(EProjectLevel.ENTRY_LEVEL));
        m_professionLevelRepository.save(new ProjectProfessionLevel(EProjectProfessionLevel.Expert));
        m_sectorRepository.save(new Sector(ESector.IT));
        m_degreeRepository.save(new Degree(EDegree.BACHELOR));
        m_interviewTypeRepository.save(new InterviewType(EInterviewType.CODE));

        m_userRepository.save(new User(UUID.randomUUID(), "nuricanozturk", "canozturk309@gmail.com", "Nuri", "Can", "OZTURK"));
        m_userRepository.save(new User(UUID.randomUUID(), "halilozturk", "halil@gmail.com", "Halil", "Can", "OZTURK"));

    }

    @Test
    @Order(2)
    public void loadProjects()
    {
        var accessType = m_accessTypeRepository.findProjectAccessTypeByProjectAccessType(EProjectAccessType.PUBLIC).get();
        var projectLevel = m_levelRepository.findProjectLevelByProjectLevel(EProjectLevel.ENTRY_LEVEL).get();
        var profLevel = m_professionLevelRepository.findProjectProfessionLevelByProjectProfessionLevel(EProjectProfessionLevel.Expert).get();
        var sector = m_sectorRepository.findSectorBySector(ESector.IT).get();
        var degree = m_degreeRepository.findDegreeByDegree(EDegree.BACHELOR).get();
        var interviewType = m_interviewTypeRepository.findInterviewTypeByInterviewType(EInterviewType.CODE).get();
        var user1 = m_userRepository.findByUsername("nuricanozturk").get();
        var user2 = m_userRepository.findByUsername("halilozturk").get();
        // Project 1 and 2 are owned by user 1, project 3 is owned by user 2
        m_projectRepository.save(new Project.Builder()
                .setProjectId(UUID.randomUUID())
                .setProjectAccessType(accessType)
                .setProjectOwner(user2)
                .setProjectLevel(projectLevel)
                .setProfessionLevel(profLevel)
                .setSector(sector)
                .setDegree(degree)
                .setInterviewType(interviewType)
                .setProjectAim("Aim - 1")
                .setDescription("Desc - 1")
                .setExpectedCompletionDate(LocalDate.now().plusMonths(9))
                .setApplicationDeadline(LocalDate.now().plusMonths(5))
                .setProjectImagePath("Image - 1")
                .setProjectName("Name - 1")
                .setProjectSummary("Summary - 1")
                .setSpecialRequirements("Special - 1")
                .setTechnicalRequirements("Technical - 1")
                .setMaxParticipant(5)
                .build());
        m_projectRepository.save(new Project.Builder()
                .setProjectAccessType(accessType)
                .setProjectOwner(user1)
                .setProjectLevel(projectLevel)
                .setProfessionLevel(profLevel)
                .setSector(sector)
                .setDegree(degree)
                .setInterviewType(interviewType)
                .setProjectAim("Aim - 2")
                .setDescription("Desc - 2")
                .setExpectedCompletionDate(LocalDate.now().plusMonths(9))
                .setApplicationDeadline(LocalDate.now().plusMonths(5))
                .setProjectImagePath("Image - 2")
                .setProjectName("Name - 2")
                .setProjectSummary("Summary - 2")
                .setSpecialRequirements("Special - 2")
                .setTechnicalRequirements("Technical - 2")
                .setMaxParticipant(5)
                .build());

        m_projectRepository.save(new Project.Builder()
                .setProjectAccessType(accessType)
                .setProjectOwner(user2)
                .setProjectLevel(projectLevel)
                .setProfessionLevel(profLevel)
                .setSector(sector)
                .setDegree(degree)
                .setInterviewType(interviewType)
                .setProjectAim("Aim - 3")
                .setDescription("Desc - 3")
                .setExpectedCompletionDate(LocalDate.now().plusMonths(9))
                .setApplicationDeadline(LocalDate.now().plusMonths(5))
                .setProjectImagePath("Image - 3")
                .setProjectName("Name - 3")
                .setProjectSummary("Summary - 3")
                .setSpecialRequirements("Special - 3")
                .setTechnicalRequirements("Technical - 3")
                .setMaxParticipant(5)
                .build());

    }

}
