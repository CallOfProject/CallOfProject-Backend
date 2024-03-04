package callofproject.dev.service.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import static callofproject.dev.data.interview.BeanName.*;
import static callofproject.dev.service.interview.ServiceBeanName.JWT_SERVICE_BEAN_NAME;
import static callofproject.dev.service.interview.ServiceBeanName.SERVICE_BEAN_NAME;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {BASE_PACKAGE, SERVICE_BEAN_NAME, JWT_SERVICE_BEAN_NAME})
@EnableJpaRepositories(basePackages = REPOSITORY_PACKAGE)
@EntityScan(basePackages = {ENTITY_PACKAGE})
public class InterviewServiceApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(InterviewServiceApplication.class, args);
    }

}
