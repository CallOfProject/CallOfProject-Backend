package callofproject.dev.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class WebClientConfig
{

    @Bean
    public RestTemplate provideRestTemplate()
    {
        return new RestTemplate();
    }

}
