package callofproject.dev.authentication;

import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.Operation;
import callofproject.dev.authentication.dto.UserKafkaDTO;
import callofproject.dev.nosql.dal.MatchServiceHelper;
import callofproject.dev.nosql.repository.IUserTagRepository;
import callofproject.dev.repository.authentication.entity.Role;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.entity.UserProfile;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import callofproject.dev.repository.authentication.repository.rdbms.IUserProfileRepository;
import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

import static callofproject.dev.authentication.util.Util.BASE_PACKAGE;
import static callofproject.dev.authentication.util.Util.REPO_PACKAGE;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {REPO_PACKAGE, BASE_PACKAGE, "callofproject.dev.service.jwt", "callofproject.dev.nosql"})
@EnableJpaRepositories(basePackages = REPO_PACKAGE) // Enable RDBMS ORM entities
@EnableMongoRepositories(basePackages = {REPO_PACKAGE, "callofproject.dev.nosql"}) // Enable NoSQL ORM entities
@EntityScan(basePackages = {REPO_PACKAGE, "callofproject.dev.nosql"})
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class AuthenticationServiceApplication implements ApplicationRunner
{
    private final IUserRepository m_userRepository;
    private final PasswordEncoder m_passwordEncoder;
    private final IUserTagRepository m_userTagRepository;
    private final MatchServiceHelper m_serviceHelper;

    private final IUserProfileRepository m_userProfileRepository;
    private final KafkaProducer m_kafkaProducer;

    public AuthenticationServiceApplication(IUserRepository userRepository, PasswordEncoder passwordEncoder, IUserTagRepository userTagRepository, MatchServiceHelper serviceHelper, IUserProfileRepository userProfileRepository, KafkaProducer kafkaProducer)
    {
        m_userRepository = userRepository;
        m_passwordEncoder = passwordEncoder;
        m_userTagRepository = userTagRepository;
        m_serviceHelper = serviceHelper;
        m_userProfileRepository = userProfileRepository;
        m_kafkaProducer = kafkaProducer;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception
    {

        if (m_userRepository.findByUsername("cop_root").isEmpty())
        {
            var rootUser = new User("cop_root", "root", "root", "root", "nuricanozturk02@gmail.com",
                    m_passwordEncoder.encode("cop123"), LocalDate.now(), new Role(RoleEnum.ROLE_ROOT.getRole()));

            rootUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
            rootUser.addRoleToUser(new Role(RoleEnum.ROLE_ADMIN.getRole()));
            var profile1 = new UserProfile();
            profile1.setUser(rootUser);
            rootUser.setUserProfile(profile1);

            m_userRepository.save(rootUser);

            var adminUser = new User("cop_admin", "admin", "admin", "admin", "nuricanozturk01@gmail.com",
                    m_passwordEncoder.encode("cop_123"), LocalDate.now(), new Role(RoleEnum.ROLE_ADMIN.getRole()));
            adminUser.setUserProfile(new UserProfile());
            adminUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));

            var profile2 = new UserProfile();
            profile2.setUser(adminUser);
            adminUser.setUserProfile(profile2);
            m_userRepository.save(adminUser);

            m_kafkaProducer.sendMessage(new UserKafkaDTO(rootUser.getUserId(), rootUser.getUsername(), rootUser.getEmail(),
                    rootUser.getFirstName(), rootUser.getMiddleName(), rootUser.getLastName(), Operation.CREATE, 0, 0, 0));

            m_kafkaProducer.sendMessage(new UserKafkaDTO(adminUser.getUserId(), adminUser.getUsername(), adminUser.getEmail(),
                    adminUser.getFirstName(), adminUser.getMiddleName(), adminUser.getLastName(), Operation.CREATE, 0, 0, 0));
        }
    }
}
