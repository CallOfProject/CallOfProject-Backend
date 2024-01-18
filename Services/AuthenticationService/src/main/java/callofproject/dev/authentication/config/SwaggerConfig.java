package callofproject.dev.authentication.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * This class is a Spring Configuration class that provides configuration for Swagger documentation.
 */
@Configuration
public class SwaggerConfig
{
    /**
     * Constructs a new SwaggerConfig.
     */
    public SwaggerConfig()
    {
    }

    /**
     * Creates and configures the OpenAPI object.
     *
     * @return Configured OpenAPI object.
     */
    @Bean
    public OpenAPI openAPI()
    {
        return new OpenAPI().info(new Info().title("Authentication Service")
                .version("1.0.0").contact(new Contact().name("Call Of Project Teams"))
                .description("Authentication Service for Call Of Project"));
    }
}
