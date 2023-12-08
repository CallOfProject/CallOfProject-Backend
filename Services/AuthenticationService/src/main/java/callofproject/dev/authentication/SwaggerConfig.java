package callofproject.dev.authentication;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig
{
    @Bean
    public OpenAPI openAPI()
    {
        return new OpenAPI().info(new Info().title("Authentication Service")
                .version("1.0.0").contact(new Contact().name("Call Of Project Teams"))
                .description("Authentication Service for Call Of Project"));
    }
}
