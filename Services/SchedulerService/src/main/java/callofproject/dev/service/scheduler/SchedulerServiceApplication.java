package callofproject.dev.service.scheduler;

import callofproject.dev.data.interview.BeanName;
import callofproject.dev.data.project.ProjectRepositoryBeanName;
import callofproject.dev.data.task.TaskServiceBeanName;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static callofproject.dev.service.scheduler.SchedulerBeanName.*;

@SpringBootApplication
@EnableDiscoveryClient

@ComponentScan(basePackages = {BASE_BEAN_NAME,
        ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME, AUTHENTICATION_COMPONENT_NAME,
        BeanName.BASE_PACKAGE, TaskServiceBeanName.BASE_BEAN_NAME})

@EntityScan(basePackages = {PROJECT_ENTITY_PACKAGE_NAME, AUTHENTICATION_ENTITY_PACKAGE_NAME,
        BeanName.ENTITY_PACKAGE, TaskServiceBeanName.ENTITY_BEAN_NAME})

@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)

@EnableFeignClients
public class SchedulerServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(SchedulerServiceApplication.class, args);
    }
}
