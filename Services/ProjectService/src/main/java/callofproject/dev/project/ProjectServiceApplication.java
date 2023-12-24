package callofproject.dev.project;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import static callofproject.dev.data.project.ProjectRepositoryBeanName.BASE_PACKAGE_BEAN_NAME;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.project.util.Constants.SERVICE_BASE_PACKAGE;


@SpringBootApplication
@EnableDiscoveryClient
@ComponentScan(basePackages = {SERVICE_BASE_PACKAGE, BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EnableJpaRepositories(basePackages = {BASE_PACKAGE_BEAN_NAME, NO_SQL_REPOSITORY_BEAN_NAME})
@EnableMongoRepositories(basePackages = NO_SQL_REPOSITORY_BEAN_NAME) // Enable NoSQL ORM entities
@EntityScan(basePackages = BASE_PACKAGE_BEAN_NAME)
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class ProjectServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(ProjectServiceApplication.class, args);
    }
}
