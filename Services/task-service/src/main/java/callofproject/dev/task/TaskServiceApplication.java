package callofproject.dev.task;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.task", "callofproject.dev.data.task"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.task", "callofproject.dev.data.task"})
@EntityScan(basePackages = "callofproject.dev.data.task")
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class TaskServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}


