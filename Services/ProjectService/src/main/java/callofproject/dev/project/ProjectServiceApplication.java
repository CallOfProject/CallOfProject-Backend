package callofproject.dev.project;

import callofproject.dev.data.project.dal.ProjectServiceHelper;
import callofproject.dev.nosql.dal.ProjectTagServiceHelper;
import callofproject.dev.nosql.entity.ProjectTag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.List;
import java.util.UUID;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.data.project.ProjectRepositoryBeanName.PROJECT_SERVICE_HELPER_BEAN;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.PROJECT_TAG_SERVICE_HELPER_BEAN_NAME;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EnableJpaRepositories(basePackages = {BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EnableMongoRepositories(basePackages = NO_SQL_REPOSITORY_BEAN_NAME) // Enable NoSQL ORM entities
@EntityScan(basePackages = BASE_PACKAGE_BEAN_NAME)
//@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class ProjectServiceApplication implements ApplicationRunner
{
    private final ProjectTagServiceHelper m_tagProjectRepository;

    private final ProjectServiceHelper m_serviceHelper;

    public ProjectServiceApplication(@Qualifier(PROJECT_TAG_SERVICE_HELPER_BEAN_NAME) ProjectTagServiceHelper tagProjectRepository,
                                     @Qualifier(PROJECT_SERVICE_HELPER_BEAN) ProjectServiceHelper serviceHelper)
    {
        m_tagProjectRepository = tagProjectRepository;
        m_serviceHelper = serviceHelper;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {

       /* var tag1 = new ProjectTag("JAVA", UUID.fromString("9ad481f8-eb21-4d40-9c76-013e9c499fd2"));
        var tag2 = new ProjectTag("Spring BOOT", UUID.fromString("9ad481f8-eb21-4d40-9c76-013e9c499fd2"));

        var tag3 = new ProjectTag("C#", UUID.fromString("61ba6025-5641-4ca8-aebe-8d32ad91994c"));
        var tag4 = new ProjectTag("ASP .NET CORE", UUID.fromString("61ba6025-5641-4ca8-aebe-8d32ad91994c"));

        m_tagProjectRepository.saveAll(List.of(tag1, tag2, tag3, tag4));*/
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

     /*   var p1 = m_projectRepository.findAll();
        var projectTag = new ProjectTag("JAVA", p1.get(0).getProjectId());
        var projectTag2 = new ProjectTag("C#", p1.get(1).getProjectId());
        var projectTag3 = new ProjectTag("SPRING-BOOT", p1.get(0).getProjectId());
        var projectTag4 = new ProjectTag("ASP .NET CORE", p1.get(1).getProjectId());

        m_tagProjectRepository.saveProjectTag(projectTag);
        m_tagProjectRepository.saveProjectTag(projectTag2);
        m_tagProjectRepository.saveProjectTag(projectTag3);
        m_tagProjectRepository.saveProjectTag(projectTag4);

        var p = StreamSupport.stream(m_serviceHelper.findAllProjectsByInterviewType(EInterviewType.CODE, 1).spliterator(), false).toList();

        p.stream().map(Project::getProjectName).forEach(System.out::println);*/
    }
}
