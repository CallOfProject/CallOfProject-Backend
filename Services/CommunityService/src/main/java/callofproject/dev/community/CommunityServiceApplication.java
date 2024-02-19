package callofproject.dev.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.community", "callofproject.dev.data.community"})
@EntityScan(basePackages = "callofproject.dev.data.community.entity")
public class CommunityServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CommunityServiceApplication.class, args);
    }
}
