package callofproject.dev.community;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import static callofproject.dev.community.CommunityServiceBeanName.*;
import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;

@EnableDiscoveryClient
@SpringBootApplication
@ComponentScan(basePackages = {BASE_PACKAGE, REPOSITORY_PACKAGE, NO_SQL_REPOSITORY_BEAN_NAME})
@EnableJpaRepositories(basePackages = {BASE_PACKAGE, REPOSITORY_PACKAGE})
@EnableMongoRepositories(basePackages = NO_SQL_REPOSITORY_BEAN_NAME)
@EntityScan(basePackages = ENTITY_PACKAGE)
public class CommunityServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(CommunityServiceApplication.class, args);
    }
}
