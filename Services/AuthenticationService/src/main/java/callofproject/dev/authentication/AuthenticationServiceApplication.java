package callofproject.dev.authentication;

import callofproject.dev.nosql.dal.MatchServiceHelper;
import callofproject.dev.nosql.repository.IUserTagRepository;
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

    public AuthenticationServiceApplication(IUserRepository userRepository, PasswordEncoder passwordEncoder, IUserTagRepository userTagRepository, MatchServiceHelper serviceHelper)
    {
        m_userRepository = userRepository;
        m_passwordEncoder = passwordEncoder;
        m_userTagRepository = userTagRepository;
        m_serviceHelper = serviceHelper;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception
    {
        //save random user
        /*if (m_userRepository.findByUsername("cop_root").isEmpty())
        {
            var rootUser = new User("cop_root", "root", "root", "root", "canozturk309@gmail.com",
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
        } else
        {
            var usr = m_userRepository.findByUsername("cop_root");
            System.out.println("name: " + usr.get().getUsername());
            var profileUser = usr.get().getUserProfile();
            System.out.println("id: " + profileUser.getUserProfileId());
            var ptu = profileUser.getUser();
            System.out.println("Profile to user: " + ptu.getUsername());
        }*/

       /* var user = m_userRepository.findByUsername("cop_root");
        var tag = new UserTag("JAVA DEVELOPER", user.get().getUserId());
        m_userTagRepository.save(tag);*/

    }
}
