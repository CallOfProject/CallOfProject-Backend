package callofproject.dev.task;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {"callofproject.dev.task", "callofproject.dev.data.task"})
@EnableJpaRepositories(basePackages = {"callofproject.dev.task", "callofproject.dev.data.task"})
@EntityScan(basePackages = "callofproject.dev.data.task")
public class TaskServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(TaskServiceApplication.class, args);
    }
}


