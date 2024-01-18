package callofproject.dev.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * ExecutorConfig
 */
@Configuration
public class ExecutorConfig
{
    /**
     * Constructs a new ExecutorConfig.
     */
    public ExecutorConfig()
    {
    }

    /**
     * Constructs a new ExecutorConfig.
     */
    @Bean
    public ExecutorService provideExecutorService()
    {
        return Executors.newFixedThreadPool(1);
    }
}
