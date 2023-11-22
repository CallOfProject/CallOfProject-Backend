package callofproject.dev.environment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "callofproject.dev.repository.environment")
@EntityScan(basePackages = "callofproject.dev.repository.environment")
@ComponentScan(basePackages = {"callofproject.dev.environment", "callofproject.dev.repository.environment"})
public class EnvironmentServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(EnvironmentServiceApplication.class, args);
    }

}
