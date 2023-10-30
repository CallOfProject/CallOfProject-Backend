package callofproject.dev.usermanagement;

import callofproject.dev.repository.usermanagement.dal.UserManagementServiceHelper;
import callofproject.dev.repository.usermanagement.entity.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.time.LocalDate;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan("callofproject.dev.repository")
@EnableJpaRepositories(basePackages = "callofproject.dev.repository")
@EntityScan(basePackages = "callofproject.dev.repository")
public class UserManagementApplication implements CommandLineRunner
{
    private final UserManagementServiceHelper m_helper;

    public UserManagementApplication(UserManagementServiceHelper helper)
    {
        m_helper = helper;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(UserManagementApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        var user = new User("can", "veli", "asd", "gfdgdfgdfgdf", "gdsfdsgds", LocalDate.now());
        m_helper.getUserServiceHelper().saveUser(user);
    }
}
