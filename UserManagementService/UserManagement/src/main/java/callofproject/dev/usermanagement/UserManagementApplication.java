package callofproject.dev.usermanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"callofproject.dev.repository", "callofproject.dev.usermanagement"})
@EnableJpaRepositories(basePackages = "callofproject.dev.repository")
@EntityScan(basePackages = "callofproject.dev.repository")
public class UserManagementApplication
{


    public static void main(String[] args)
    {
        SpringApplication.run(UserManagementApplication.class, args);
    }

}
