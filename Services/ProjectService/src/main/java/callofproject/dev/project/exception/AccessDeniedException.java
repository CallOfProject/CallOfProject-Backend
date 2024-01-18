package callofproject.dev.project.exception;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

/**
 * Custom access denied handler.
 * This class is responsible for handling Access Denied exceptions in Spring Security.
 */
public class AccessDeniedException implements AccessDeniedHandler
{
    /**
     * Default constructor.
     */
    public AccessDeniedException()
    {
    }

    /**
     * Handles an access denied failure.
     *
     * @param request               HttpServletRequest that resulted in an AccessDeniedException
     * @param response              HttpServletResponse so that the user-agent can be advised of the failure
     * @param accessDeniedException AccessDeniedException that caused the invocation
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, org.springframework.security.access.AccessDeniedException accessDeniedException) throws IOException, ServletException
    {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write("Erişim reddedildi: " + accessDeniedException.getMessage());
    }
}
