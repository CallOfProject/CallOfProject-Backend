package callofproject.dev.authentication;

import callofproject.dev.repository.authentication.repository.rdbms.IUserRepository;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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
import org.springframework.security.crypto.password.PasswordEncoder;

import static callofproject.dev.authentication.util.Util.BASE_PACKAGE;
import static callofproject.dev.authentication.util.Util.REPO_PACKAGE;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {REPO_PACKAGE, BASE_PACKAGE, "callofproject.dev.service.jwt"})
@EnableJpaRepositories(basePackages = REPO_PACKAGE) // Enable RDBMS ORM entities
@EnableMongoRepositories(basePackages = REPO_PACKAGE) // Enable NoSQL ORM entities
@EntityScan(basePackages = REPO_PACKAGE)
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

    public AuthenticationServiceApplication(IUserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        m_userRepository = userRepository;
        m_passwordEncoder = passwordEncoder;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception
    {
     /*   if (m_userRepository.findByUsername("cop_root").isEmpty())
        {
            var rootUser = new User("cop_root", "root", "root", "root", "canozturk309@gmail.com",
                    m_passwordEncoder.encode("cop123"), LocalDate.now(), new Role(RoleEnum.ROLE_ROOT.getRole()));

            rootUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
            rootUser.addRoleToUser(new Role(RoleEnum.ROLE_ADMIN.getRole()));

            m_userRepository.save(rootUser);

            var adminUser = new User("cop_admin", "admin", "admin", "admin", "nuricanozturk01@gmail.com",
                    m_passwordEncoder.encode("cop_123"), LocalDate.now(), new Role(RoleEnum.ROLE_ADMIN.getRole()));

            adminUser.addRoleToUser(new Role(RoleEnum.ROLE_USER.getRole()));
            m_userRepository.save(adminUser);
        }*/
    }
}
