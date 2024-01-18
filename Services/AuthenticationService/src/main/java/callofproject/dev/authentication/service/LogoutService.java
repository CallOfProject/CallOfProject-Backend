package callofproject.dev.authentication.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * Service class for handling logout operations.
 */
@Service("ad")
@Lazy
public class LogoutService extends SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler
{

    /**
     * Constructor for LogoutService.
     */
    public LogoutService()
    {
    }


 /*   @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        //var jwt = authHeader.substring(7);

        SecurityContextHolder.getContext().getAuthentication();
    }*/

    /**
     * Handles logout success.
     *
     * @param request        The HttpServletRequest.
     * @param response       The HttpServletResponse.
     * @param authentication The Authentication object representing the user.
     * @throws IOException      If an input or output exception occurs during the operation.
     * @throws ServletException If a servlet exception occurs during the operation.
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException
    {
        super.onLogoutSuccess(request, response, authentication);
    }
}
