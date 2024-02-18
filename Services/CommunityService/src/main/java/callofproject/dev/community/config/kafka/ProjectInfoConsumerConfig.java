package callofproject.dev.community.config.kafka;


import callofproject.dev.community.config.kafka.dto.ProjectInfoKafkaDTO;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;

@EnableKafka
@Configuration
public class ProjectInfoConsumerConfig
{
    @Value("${spring.kafka.consumer.bootstrap-servers}")
    private String m_servers;

    @Value("${spring.kafka.consumer.project-group-id}")
    private String m_projectGroupId;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String m_offsetResetConfig;

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ProjectInfoKafkaDTO> configProjectInfoKafkaListener()
    {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, ProjectInfoKafkaDTO>();
        factory.setConsumerFactory(projectConsumerConfig());
        return factory;
    }

    @Bean
    public ConsumerFactory<String, ProjectInfoKafkaDTO> projectConsumerConfig()
    {
        var props = new HashMap<String, Object>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, m_servers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, m_projectGroupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, m_offsetResetConfig);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
        props.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
        props.put(JsonDeserializer.VALUE_DEFAULT_TYPE, ProjectInfoKafkaDTO.class);
        props.put("spring.json.use.type.headers", false);
        return new DefaultKafkaConsumerFactory<>(props);
    }
}