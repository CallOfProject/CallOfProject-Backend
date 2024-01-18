package callofproject.dev.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * ApplicationConfig
 */
@Configuration
public class ApplicationConfig
{

    /**
     * Constructs a new ApplicationConfig.
     */
    public ApplicationConfig()
    {
    }

    /**
     * Creates and configures the password encoder.
     *
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}