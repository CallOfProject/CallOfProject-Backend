package callofproject.dev.service.notification;


import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

import static callofproject.dev.nosql.NoSqlBeanName.NO_SQL_REPOSITORY_BEAN_NAME;
import static callofproject.dev.service.notification.util.BeanName.NOTIFICATION_SERVICE;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan(basePackages = {NO_SQL_REPOSITORY_BEAN_NAME, NOTIFICATION_SERVICE})
@EnableMongoRepositories(basePackages = {NO_SQL_REPOSITORY_BEAN_NAME})
@EnableDiscoveryClient
@EnableWebSocket
@SecurityScheme(
        name = "Authorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class NotificationServiceApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }
}
