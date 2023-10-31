package callofproject.dev.usermanagement;

import callofproject.dev.repository.usermanagement.entity.nosql.MatchDB;
import callofproject.dev.repository.usermanagement.repository.nosql.IMatchDbRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import java.util.UUID;

import static callofproject.dev.usermanagement.Util.BASE_PACKAGE;
import static callofproject.dev.usermanagement.Util.REPO_PACKAGE;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {REPO_PACKAGE, BASE_PACKAGE})
@EnableJpaRepositories(basePackages = REPO_PACKAGE) // Enable RDBMS ORM entities
@EnableMongoRepositories(basePackages = REPO_PACKAGE) // Enable NoSQL ORM entities
@EntityScan(basePackages = REPO_PACKAGE)
public class UserManagementApplication implements CommandLineRunner
{
    private final IMatchDbRepository m_repository;

    public UserManagementApplication(IMatchDbRepository repository)
    {
        m_repository = repository;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(UserManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        //m_repository.insert(new MatchDB(UUID.randomUUID(),UUID.randomUUID(),"DENEME", "DENEME"));
        m_repository.save(new MatchDB(UUID.randomUUID(), "Yasar University", "CSD Java"));

        m_repository.findAll().stream().map(MatchDB::getSchoolName).forEach(System.out::println);
    }
}
