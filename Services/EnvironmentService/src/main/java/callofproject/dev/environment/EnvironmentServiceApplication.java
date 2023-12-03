package callofproject.dev.environment;

import callofproject.dev.environment.service.EnvironmentService;
import callofproject.dev.repository.environment.dto.UniversityDTO;
import callofproject.dev.repository.environment.entity.University;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableMongoRepositories(basePackages = {"callofproject.dev.repository.environment"}) // Enable NoSQL ORM entities
@EntityScan(basePackages = {"callofproject.dev.repository.environment", "callofproject.dev.environment"})
@ComponentScan(basePackages = {"callofproject.dev.environment", "callofproject.dev.repository.environment"})
public class EnvironmentServiceApplication implements CommandLineRunner
{
    private final EnvironmentService m_universityRepository;

    public EnvironmentServiceApplication(EnvironmentService universityRepository)
    {
        m_universityRepository = universityRepository;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(EnvironmentServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        /*var uni = new University("Yaşar Üniversitesi");
        var un2 = new University("yasar university");
        var un3 = new University("Yasar University");
        var un4 = new University("yasaruniversity");
        var un5 = new University("YASAR UNIVERSITY");

        m_universityRepository.saveUniversity(new UniversityDTO(uni.getUniversityName()));
        m_universityRepository.saveUniversity(new UniversityDTO(un2.getUniversityName()));
        m_universityRepository.saveUniversity(new UniversityDTO(un3.getUniversityName()));
        m_universityRepository.saveUniversity(new UniversityDTO(un4.getUniversityName()));
        m_universityRepository.saveUniversity(new UniversityDTO(un5.getUniversityName()));*/
    }
}
