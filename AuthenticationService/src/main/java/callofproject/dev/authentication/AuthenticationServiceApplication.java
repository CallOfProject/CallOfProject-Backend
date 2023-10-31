package callofproject.dev.authentication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.authentication", "callofproject.dev.repository.usermanagement"})
@EnableJpaRepositories(basePackages = "callofproject.dev.repository.usermanagement")
@EntityScan(basePackages = "callofproject.dev.repository.usermanagement")
public class AuthenticationServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(AuthenticationServiceApplication.class, args);
    }

}
