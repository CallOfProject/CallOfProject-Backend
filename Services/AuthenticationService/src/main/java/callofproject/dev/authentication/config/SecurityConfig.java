package callofproject.dev.authentication.config;

import callofproject.dev.service.jwt.filter.JWTTokenGeneratorFilter;
import callofproject.dev.service.jwt.filter.JWTTokenValidatorFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
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
 * This class is a Spring Configuration class that provides configuration for security.
 */
@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig
{
    private static final String[] AUTH_WHITELIST = {
            "/api/auth/forgot-password/**",
            "/api/auth/authenticate/register",
            "/api/auth/authenticate/register/mobile",
            "/api/auth/authenticate/register/verify",
            "/swagger-ui/**",
            "/api/auth/authenticate/login",
            "/api/auth/admin/login"
    };

    private static final String[] ROOT_AND_ADMIN_WHITE_LIST = {
            "/api/auth/authenticate/refresh-token",
            "/api/auth/authenticate/validate",
            "/api/auth/admin/**"
    };
    private final AuthenticationProvider authenticationProvider;
    //private final LogoutSuccessHandler logoutHandler;


    /**
     * Constructs a new SecurityConfig.
     *
     * @param authenticationProvider The AuthenticationProvider object to be injected.
     */
    public SecurityConfig(AuthenticationProvider authenticationProvider)
    {
        this.authenticationProvider = authenticationProvider;
        //this.logoutHandler = logoutHandler;
    }

    /**
     * Customize security
     *
     * @param requests represent the request
     */
    private static void customize(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry requests)
    {
        requests//.requestMatchers("/api/auth/register-all").permitAll()
                .requestMatchers(antMatcher("/api-docs/**")).permitAll()
                .requestMatchers(AUTH_WHITELIST).permitAll()
                .requestMatchers(ROOT_AND_ADMIN_WHITE_LIST).hasAnyRole("ROOT", "ADMIN")
                .requestMatchers("/api/auth/authenticate/logout").authenticated()
                .requestMatchers("/api/auth/users/**").hasAnyRole("USER", "ADMIN", "ROOT")
                .requestMatchers("/api/auth/user-info/**").hasAnyRole("USER", "ADMIN", "ROOT")
                .requestMatchers("/api/auth/root/**").hasAnyRole("ROOT");
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
                //.cors(c -> c.configurationSource(this::setCorsConfig))
                .csrf(AbstractHttpConfigurer::disable);

        http.addFilterAfter(new JWTTokenGeneratorFilter(), BasicAuthenticationFilter.class)
                .addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests(SecurityConfig::customize)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .authenticationProvider(authenticationProvider);

      /*  http.logout(logout -> logout.logoutUrl("/api/auth/logout")
                .addLogoutHandler(logoutHandler)
                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()));*/

        return http.build();
    }
}