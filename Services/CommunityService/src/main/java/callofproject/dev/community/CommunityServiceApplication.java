package callofproject.dev.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static callofproject.dev.community.CommunityServiceBeanName.*;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {BASE_PACKAGE, REPOSITORY_PACKAGE})
@EnableJpaRepositories(basePackages = {BASE_PACKAGE, REPOSITORY_PACKAGE})
@EntityScan(basePackages = ENTITY_PACKAGE)
public class CommunityServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CommunityServiceApplication.class, args);
    }
}
