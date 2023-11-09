package callofproject.dev.authentication.service;

import callofproject.dev.repository.authentication.dal.TokenServiceHelper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import static callofproject.dev.repository.authentication.BeanName.TOKEN_DAL_BEAN;

@Service
@Lazy
public class LogoutService implements LogoutHandler
{
    private final TokenServiceHelper m_tokenServiceHelper;

    public LogoutService(@Qualifier(TOKEN_DAL_BEAN) TokenServiceHelper tokenServiceHelper)
    {
        m_tokenServiceHelper = tokenServiceHelper;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
    {
        final String authHeader = request.getHeader("Authorization");
        final String jwt;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        jwt = authHeader.substring(7);

        var storedToken = m_tokenServiceHelper.findByToken(jwt).orElse(null);

        if (storedToken != null)
        {
            storedToken.setExpired(true);
            storedToken.setRevoked(true);
            m_tokenServiceHelper.saveToken(storedToken);
            SecurityContextHolder.clearContext();
        }
    }
}
