package callofproject.dev.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static callofproject.dev.usermanagement.Util.BASE_PACKAGE;
import static callofproject.dev.usermanagement.Util.REPO_PACKAGE;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {REPO_PACKAGE, BASE_PACKAGE})
@EnableJpaRepositories(basePackages = REPO_PACKAGE)
@EntityScan(basePackages = REPO_PACKAGE)
public class UserManagementApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(UserManagementApplication.class, args);
    }
}
