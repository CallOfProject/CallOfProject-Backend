package callofproject.dev.project;

import callofproject.dev.data.project.entity.*;
import callofproject.dev.data.project.entity.enums.*;
import callofproject.dev.data.project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
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


    public static void main(String[] args)
    {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        if (m_accessTypeRepository.count() > 0)
            return;

        m_accessTypeRepository.save(new ProjectAccessType(EProjectAccessType.PUBLIC));
        m_accessTypeRepository.save(new ProjectAccessType(EProjectAccessType.PRIVATE));

        m_levelRepository.save(new ProjectLevel(EProjectLevel.ENTRY_LEVEL));
        m_levelRepository.save(new ProjectLevel(EProjectLevel.INTERMEDIATE));
        m_levelRepository.save(new ProjectLevel(EProjectLevel.EXPERT));

        m_professionLevelRepository.save(new ProjectProfessionLevel(EProjectProfessionLevel.Entry_Level));
        m_professionLevelRepository.save(new ProjectProfessionLevel(EProjectProfessionLevel.Intermediate));
        m_professionLevelRepository.save(new ProjectProfessionLevel(EProjectProfessionLevel.Expert));

        m_sectorRepository.save(new Sector(ESector.IT));
        m_sectorRepository.save(new Sector(ESector.Marketing));
        m_sectorRepository.save(new Sector(ESector.Finance));
        m_sectorRepository.save(new Sector(ESector.Human_Resources));
        m_sectorRepository.save(new Sector(ESector.Sales));

        m_degreeRepository.save(new Degree(EDegree.BACHELOR));
        m_degreeRepository.save(new Degree(EDegree.MASTER));
        m_degreeRepository.save(new Degree(EDegree.PHD));

        m_interviewTypeRepository.save(new InterviewType(EInterviewType.CODE));
        m_interviewTypeRepository.save(new InterviewType(EInterviewType.TEST));
        m_interviewTypeRepository.save(new InterviewType(EInterviewType.NO_INTERVIEW));
    }
}
