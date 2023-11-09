package callofproject.dev.authentication.config;


import callofproject.dev.repository.authentication.dal.TokenServiceHelper;
import callofproject.dev.service.jwt.JwtService;
import callofproject.dev.service.jwt.JwtServiceBeanName;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static callofproject.dev.repository.authentication.BeanName.TOKEN_DAL_BEAN;
import static callofproject.dev.service.jwt.JwtServiceBeanName.JWT_SERVICE_BEAN_NAME;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter
{
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenServiceHelper m_tokenServiceHelper;

    public JwtAuthenticationFilter(@Qualifier(JWT_SERVICE_BEAN_NAME) JwtService jwtService,
                                   UserDetailsService userDetailsService,
                                   @Qualifier(TOKEN_DAL_BEAN) TokenServiceHelper tokenServiceHelper)
    {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;

        m_tokenServiceHelper = tokenServiceHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException
    {
        if (request.getServletPath().contains("/api/auth"))
        {
            filterChain.doFilter(request, response);
            return;
        }
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);

        userEmail = jwtService.extractUsername(jwt);

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            var isTokenValid = m_tokenServiceHelper.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);

            if (jwtService.isTokenValid(jwt, userDetails.getUsername()) && isTokenValid)
            {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
