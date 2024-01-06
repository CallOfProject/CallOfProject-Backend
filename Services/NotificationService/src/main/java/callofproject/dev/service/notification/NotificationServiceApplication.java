package callofproject.dev.service.notification;


import callofproject.dev.nosql.repository.INotificationRepository;
import callofproject.dev.service.notification.dto.NotificationObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.CommandLineRunner;
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
@EnableWebSocketMessageBroker
public class NotificationServiceApplication implements CommandLineRunner
{
    private final INotificationRepository m_notificationRepository;
    private final ObjectMapper m_objectMapper;

    public NotificationServiceApplication(INotificationRepository notificationRepository, ObjectMapper objectMapper)
    {
        m_notificationRepository = notificationRepository;

        m_objectMapper = objectMapper;
    }

    public static void main(String[] args)
    {
        SpringApplication.run(NotificationServiceApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        var notifications = m_notificationRepository.findAll();
        for (var notification : notifications)
        {
            try
            {
                var data = notification.getNotificationData().toString();
                //var str = JsonUtil.convertObjectToJsonString(data);
                var mapper = m_objectMapper.readValue(data, NotificationObject.class);
                System.out.println(mapper);

            }
            catch (Exception ex)
            {
                System.err.println(ex.getMessage());
            }
        }
    }
}


/*

    // Örnek Object nesnesi
Object jsonObject = getYourJsonObject(); // Bu metod, JSON object'inizi nasıl aldığınıza bağlı

// Object'i JSON String'e dönüştürme
String jsonString = JsonUtil.convertObjectToJsonString(jsonObject);

// JSON String'i Java nesnesine deserialize etme
MyData data = JsonUtil.deserializeJson(jsonString, MyData.class);


 */