package callofproject.dev.authentication.config;

import callofproject.dev.service.jwt.filter.JWTTokenGeneratorFilter;
import callofproject.dev.service.jwt.filter.JWTTokenValidatorFilter;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutHandler;
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
    private static final String[] AUTH_WHITELIST = {
            "/swagger-resources/",
            "/swagger-ui/",
            "/v3/api-docs/"
    };
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    public SecurityConfig(AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler)
    {
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    private static void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests)
    {
        requests
                .requestMatchers("/api/auth/register").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .requestMatchers(antMatcher("/api-docs/**")).permitAll()
                .requestMatchers("/api/auth/login").permitAll()
                .requestMatchers("/api/admin/login").permitAll()
                .requestMatchers("/api/auth/refresh-token").hasAnyRole("ROOT", "ADMIN")
                .requestMatchers("/api/auth/validate").hasAnyRole("ROOT", "ADMIN")
                .requestMatchers("/api/users/**").hasAnyRole("USER")
                .requestMatchers("/api/root/**").hasAnyRole("ROOT")
                .requestMatchers("/api/admin/**").hasAnyRole("ROOT", "ADMIN");
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception
    {

         /* CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
          requestHandler.setCsrfRequestAttributeName("_csrf");*/

        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(corsCustomizer -> corsCustomizer.configurationSource(this::setCorsConfig))
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(SecurityConfig::customize)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider);

        http.logout(logout -> logout.logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));

        return http.build();
    }


    private CorsConfiguration setCorsConfig(HttpServletRequest httpServletRequest)
    {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "http://192.168.1.13:3000"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setExposedHeaders(List.of("Authorization"));
        config.setMaxAge(3600L);
        return config;
    }
}
