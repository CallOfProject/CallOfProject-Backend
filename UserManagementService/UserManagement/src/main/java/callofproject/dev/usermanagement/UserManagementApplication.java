package callofproject.dev.usermanagement;

import callofproject.dev.repository.usermanagement.entity.nosql.MatchDB;
import callofproject.dev.repository.usermanagement.repository.nosql.IMatchDbRepository;
import callofproject.dev.repository.usermanagement.repository.rdbms.IEducationRepository;
import callofproject.dev.repository.usermanagement.repository.rdbms.IUserProfileRepository;
import callofproject.dev.repository.usermanagement.repository.rdbms.IUserRepository;
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
    private final IEducationRepository m_educationRepository;

    private final IUserRepository m_userRepository;

    private final IUserProfileRepository m_userProfileRepository;

    public UserManagementApplication(IMatchDbRepository repository, IEducationRepository educationRepository, IUserRepository userRepository, IUserProfileRepository userProfileRepository)
    {
        m_repository = repository;
        m_educationRepository = educationRepository;
        m_userRepository = userRepository;
        m_userProfileRepository = userProfileRepository;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(UserManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        /*var school = m_universityRepository.findByUniversityName("YAŞAR ÜNİVERSİTESİ");

        var user = new User("nuricanodzturk", "Nuri", "Can", "ÖZTÜRK", "cand@mail.com", "123", LocalDate.now());

        var profile = new UserProfile("no_cv", "image.png", "no");
        user.setUserProfile(profile);

        var education = new Education(school.get().getUniversityId(), school.get().getUniversityName(),
                "Engineering", "", LocalDate.now(), LocalDate.now().plusYears(4), true);

        profile.addEducation(education);
        m_userRepository.save(user);*/

        //var school = m_universityRepository.findByUniversityName("YAŞAR ÜNİVERSİTESİ");
        var user = m_userRepository.findByEmail("cand@mail.com").get();
        //m_repository.save(new MatchDB(user.getUserId(), school.get().getUniversityId(), UUID.randomUUID(), UUID.randomUUID()));
        m_repository.findAll().stream().map(MatchDB::getSchoolId).forEach(System.out::println);

       /* var school1 = new Education("Yasar University", "Engineering", "", now(), now());
        var school2 = new Education("Ege University", "Engineering", "", now(), now());
        var course = new Course();
        m_repository.save(new MatchDB(UUID.randomUUID(), "Yasar University", "CSD Java"));

        m_repository.findAll().stream().map(MatchDB::getSchoolName).forEach(System.out::println);*/
    }
}
