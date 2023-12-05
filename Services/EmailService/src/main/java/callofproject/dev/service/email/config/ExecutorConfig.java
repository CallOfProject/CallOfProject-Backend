package callofproject.dev.service.email.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.Executors.newCachedThreadPool;

@Component
public class ExecutorConfig
{
    @Bean
    public ExecutorService provideExecutorService()
    {
        return newCachedThreadPool();
    }
}
