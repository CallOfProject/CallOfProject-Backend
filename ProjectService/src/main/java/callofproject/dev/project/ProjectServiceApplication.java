package callofproject.dev.project;

import callofproject.dev.repository.repository.project.entity.nosql.ProjectTag;
import callofproject.dev.repository.repository.project.repository.*;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"callofproject.dev.project", "callofproject.dev.repository.repository.project"})
@EnableJpaRepositories(basePackages = "callofproject.dev.repository.repository.project") // Enable RDBMS ORM entities
@EnableMongoRepositories(basePackages = "callofproject.dev.repository.repository.project") // Enable NoSQL ORM entities
@EntityScan(basePackages = "callofproject.dev.repository.repository.project")
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ProjectServiceApplication implements ApplicationRunner
{
    private final IProjectRepository m_projectRepository;
    private final IProjectAccessTypeRepository m_accessTypeRepository;
    private final IDegreeRepository m_degreeRepository;
    private final IProjectProfessionLevelRepository m_projectProfessionLevelRepository;
    private final ISectorRepository m_sectorRepository;
    private final IProjectLevelRepository m_projectLevelRepository;
    private final IInterviewTypeRepository m_interviewTypeRepository;
    private final ITagProjectRepository m_tagProjectRepository;

    public ProjectServiceApplication(IProjectRepository projectRepository, IProjectAccessTypeRepository accessTypeRepository, IDegreeRepository degreeRepository, IProjectProfessionLevelRepository projectProfessionLevelRepository, ISectorRepository sectorRepository, IProjectLevelRepository projectLevelRepository, IInterviewTypeRepository interviewTypeRepository, ITagProjectRepository tagProjectRepository)
    {
        m_projectRepository = projectRepository;
        m_accessTypeRepository = accessTypeRepository;
        m_degreeRepository = degreeRepository;
        m_projectProfessionLevelRepository = projectProfessionLevelRepository;
        m_sectorRepository = sectorRepository;
        m_projectLevelRepository = projectLevelRepository;
        m_interviewTypeRepository = interviewTypeRepository;
        m_tagProjectRepository = tagProjectRepository;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {

        // Örnek ProjectAccessType, ProjectProfessionLevel, Sector, Degree, ProjectLevel ve InterviewType değerleri oluşturun
       /* EProjectAccessType projectAccessType1 = EProjectAccessType.PUBLIC;
        EProjectAccessType projectAccessType2 = EProjectAccessType.PRIVATE;
        EProjectProfessionLevel professionLevel1 = EProjectProfessionLevel.Expert;
        EProjectProfessionLevel professionLevel2 = EProjectProfessionLevel.Intermediate;
        ESector sector1 = ESector.IT;
        ESector sector2 = ESector.Marketing;
        EDegree degree1 = EDegree.BACHELOR;
        EDegree degree2 = EDegree.MASTER;
        EProjectLevel projectLevel1 = EProjectLevel.ENTRY_LEVEL;
        EProjectLevel projectLevel2 = EProjectLevel.INTERMEDIATE;
        EInterviewType interviewType1 = EInterviewType.CODE;
        EInterviewType interviewType2 = EInterviewType.TEST;


        ProjectAccessType projectAccessType1Entity = m_accessTypeRepository.save(new ProjectAccessType(projectAccessType1));
        ProjectAccessType projectAccessType2Entity = m_accessTypeRepository.save(new ProjectAccessType(projectAccessType2));

        ProjectProfessionLevel professionLevel1Entity = m_projectProfessionLevelRepository.save(new ProjectProfessionLevel(professionLevel1));
        ProjectProfessionLevel professionLevel2Entity = m_projectProfessionLevelRepository.save(new ProjectProfessionLevel(professionLevel2));

        Sector sector1Entity = m_sectorRepository.save(new Sector(sector1));
        Sector sector2Entity = m_sectorRepository.save(new Sector(sector2));

        Degree degree1Entity = m_degreeRepository.save(new Degree(degree1));
        Degree degree2Entity = m_degreeRepository.save(new Degree(degree2));

        ProjectLevel projectLevel1Entity = m_projectLevelRepository.save(new ProjectLevel(projectLevel1));
        ProjectLevel projectLevel2Entity = m_projectLevelRepository.save(new ProjectLevel(projectLevel2));

        InterviewType interviewType1Entity = m_interviewTypeRepository.save(new InterviewType(interviewType1));
        InterviewType interviewType2Entity = m_interviewTypeRepository.save(new InterviewType(interviewType2));


        // 5 adet Project örneği oluşturun
        Project project1 = new Project.Builder()
                .setProjectImagePath("image1.jpg")
                .setProjectName("Project 1")
                .setProjectSummary("Summary 1")
                .setDescription("Description 1")
                .setProjectAim("Aim 1")
                .setApplicationDeadline(LocalDate.now().plusDays(30))
                .setExpectedCompletionDate(LocalDate.now().plusMonths(3))
                .setExpectedProjectDeadline(LocalDate.now().plusMonths(6))
                .setMaxParticipant(10)
                .setProjectAccessType(projectAccessType1Entity)
                .setProfessionLevel(professionLevel1Entity)
                .setSector(sector1Entity)
                .setDegree(degree1Entity)
                .setProjectLevel(projectLevel1Entity)
                .setInterviewType(interviewType1Entity)
                .setInviteLink("Link 1")
                .setTechnicalRequirements("Tech Requirements 1")
                .setSpecialRequirements("Special Requirements 1")
                .build();

        Project project2 = new Project.Builder()
                .setProjectImagePath("image2.jpg")
                .setProjectName("Project 2")
                .setProjectSummary("Summary 2")
                .setDescription("Description 2")
                .setProjectAim("Aim 2")
                .setApplicationDeadline(LocalDate.now().plusDays(45))
                .setExpectedCompletionDate(LocalDate.now().plusMonths(4))
                .setExpectedProjectDeadline(LocalDate.now().plusMonths(7))
                .setMaxParticipant(15)
                .setProjectAccessType(projectAccessType2Entity)
                .setProfessionLevel(professionLevel2Entity)
                .setSector(sector2Entity)
                .setDegree(degree2Entity)
                .setProjectLevel(projectLevel2Entity)
                .setInterviewType(interviewType2Entity)
                .setInviteLink("Link 2")
                .setTechnicalRequirements("Tech Requirements 2")
                .setSpecialRequirements("Special Requirements 2")
                .build();

        var saveP1 = m_projectRepository.save(project1);
        var saveP2 = m_projectRepository.save(project2);*/

      /*    var p1 = m_projectRepository.findAll();
           var projectTag = new ProjectTag("JAVA", p1.get(0).getProjectId());
        var projectTag2 = new ProjectTag("C#", p1.get(1).getProjectId());
        var projectTag3 = new ProjectTag("SPRING-BOOT", p1.get(0).getProjectId());
        var projectTag4 = new ProjectTag("ASP .NET CORE", p1.get(1).getProjectId());

        m_tagProjectRepository.save(projectTag3);
        m_tagProjectRepository.save(projectTag4);*/

    }
}
