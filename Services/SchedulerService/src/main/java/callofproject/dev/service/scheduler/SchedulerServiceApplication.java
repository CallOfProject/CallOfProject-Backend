package callofproject.dev.service.scheduler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {"callofproject.dev.service.scheduler", "callofproject.dev.data.project", "callofproject.dev.repository.authentication"})
@EntityScan(basePackages = {"callofproject.dev.data.project", "callofproject.dev.repository.authentication"})
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SchedulerServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SchedulerServiceApplication.class, args);
    }
}
