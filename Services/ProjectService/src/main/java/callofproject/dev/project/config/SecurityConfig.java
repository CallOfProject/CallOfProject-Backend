package callofproject.dev.project.config;

import callofproject.dev.service.jwt.filter.JWTTokenGeneratorFilter;
import callofproject.dev.service.jwt.filter.JWTTokenValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;


@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig
{

    /**
     * Customize security
     *
     * @param requests represent the request
     */
    private static void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests)
    {
        requests
                .requestMatchers(antMatcher("/api/auth/register-all")).permitAll()
                .requestMatchers(antMatcher("/api-docs/**")).permitAll()
                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(antMatcher("/api/admin/project/**")).hasAnyRole("ADMIN", "USER")
                .requestMatchers(antMatcher("/api/project/**")).hasAnyRole("ROOT", "USER", "ADMIN")
                .requestMatchers(antMatcher("/api/project-owner/project/**")).hasAnyRole("ADMIN", "USER", "ROOT");
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
                .cors(corsCustomizer -> corsCustomizer.configurationSource(this::setCorsConfig))
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(SecurityConfig::customize)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable);

        return http.build();
    }


    /**
     * Set cors configuration
     *
     * @param httpServletRequest represent the request
     * @return CorsConfiguration
     */
    private CorsConfiguration setCorsConfig(HttpServletRequest httpServletRequest)
    {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000", "http://localhost:4200"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);
        return config;
    }
}