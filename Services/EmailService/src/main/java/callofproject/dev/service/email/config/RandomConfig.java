package callofproject.dev.service.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
public class RandomConfig
{
    @Bean
    public Random provideRandom()
    {
        return new SecureRandom();
    }
}
