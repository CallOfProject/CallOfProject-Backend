package callofproject.dev.service.interview.config;

import callofproject.dev.service.jwt.filter.JWTTokenGeneratorFilter;
import callofproject.dev.service.jwt.filter.JWTTokenValidatorFilter;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

/**
 * SecurityConfig
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig
{
    /**
     * SecurityConfig
     */
    public SecurityConfig()
    {
    }

    /**
     * Customize security
     *
     * @param requests represent the request
     */
    private static void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests)
    {
        requests.requestMatchers(antMatcher("/api-docs/**")).permitAll()
                .requestMatchers(antMatcher("/swagger-ui/**")).permitAll()
                .requestMatchers(antMatcher("/api/interview/coding/is-solved-before")).permitAll()
                .requestMatchers(antMatcher("/api/interview/test/is-solved-before")).permitAll()
                .requestMatchers(antMatcher("/api/interview/coding/find/by/interview-id")).permitAll()
                .requestMatchers(antMatcher("/api/interview/coding/submit")).permitAll()
                .requestMatchers(antMatcher("/api/interview/test/find/question/by/interview-id")).permitAll()
                .requestMatchers(antMatcher("/api/interview/test/submit/answer/question")).permitAll()
                .requestMatchers(antMatcher("/api/interview/test/submit")).permitAll()
                .requestMatchers(antMatcher("/api/interview/test/info/interview")).permitAll()
                .requestMatchers(antMatcher("/api/interview/coding/find/info")).permitAll()
                .requestMatchers(antMatcher("/api/interview/**")).hasAnyRole("ADMIN", "ROOT", "USER")
                .requestMatchers(antMatcher("/api/interview/management/**")).hasAnyRole("ADMIN", "ROOT", "USER");
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
                .headers(h -> h.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
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