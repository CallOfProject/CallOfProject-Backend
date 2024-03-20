package callofproject.dev.task;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static callofproject.dev.task.util.BeanName.REPOSITORY_BEAN_NAME;
import static callofproject.dev.task.util.BeanName.SERVICE_BEAN_NAME;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@EnableJpaRepositories(basePackages = {SERVICE_BEAN_NAME, REPOSITORY_BEAN_NAME})
@EntityScan(basePackages = REPOSITORY_BEAN_NAME)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@SecurityScheme(name = "Authorization", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
public class TaskServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}


