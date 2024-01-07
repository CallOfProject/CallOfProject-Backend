package callofproject.dev.service.notification.config;


import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig
{
    private static void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests)
    {
        requests
               // .requestMatchers("/api/notification/**").hasAnyRole("ADMIN", "USER", "ROOT")
                .anyRequest().permitAll();
    }


    /**
     * Configure security
     *
     * @param http represent the http
     * @return SecurityFilterChain
     * @throws Exception if something goes wrong
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                /*.cors(c -> c.configurationSource(corsConfigurationSource()))*/
                .csrf(AbstractHttpConfigurer::disable);

        http//.addFilterBefore(new JWTTokenValidatorFilter(), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(SecurityConfig::customize)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .exceptionHandling(ex -> ex.accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    response.getWriter().write("Access rejected: " + accessDeniedException.getMessage());
                }));

        return http.build();
    }


}
