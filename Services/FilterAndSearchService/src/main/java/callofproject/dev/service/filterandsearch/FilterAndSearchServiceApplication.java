package callofproject.dev.service.filterandsearch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.service.filterandsearch.SchedulerBeanName.*;

@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {BASE_BEAN_NAME, BASE_PACKAGE_BEAN_NAME, AUTHENTICATION_COMPONENT_NAME})
@EntityScan(basePackages = {BASE_BEAN_NAME, PROJECT_ENTITY_PACKAGE_NAME, AUTHENTICATION_ENTITY_PACKAGE_NAME})
public class FilterAndSearchServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(FilterAndSearchServiceApplication.class, args);
    }
}
