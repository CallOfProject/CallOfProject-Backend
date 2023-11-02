package callofproject.dev.usermanagement.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

@Configuration
@Scope("singleton")
public class RestTemplateConfig
{
    @Bean
    public RestTemplate getRestTemplate()
    {
        return new RestTemplate();
    }
}
