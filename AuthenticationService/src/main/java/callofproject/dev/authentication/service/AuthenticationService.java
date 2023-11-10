package callofproject.dev.authentication.service;

import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.BeanName;
import callofproject.dev.repository.authentication.dal.RoleServiceHelper;
import callofproject.dev.repository.authentication.dto.RoleEnum;
import callofproject.dev.repository.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.entity.AuthenticationRequest;
import callofproject.dev.authentication.entity.AuthenticationResponse;
import callofproject.dev.authentication.entity.RegisterRequest;

import callofproject.dev.service.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import static callofproject.dev.authentication.util.Util.AUTHENTICATION_SERVICE;
import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;

@Service(AUTHENTICATION_SERVICE)
@Lazy
public class AuthenticationService
{
    private final PasswordEncoder m_passwordEncoder;
    private final UserManagementService m_userManagementService;
    private final RoleServiceHelper m_roleServiceHelper;
    private final AuthenticationProvider m_authenticationProvider;


    public AuthenticationService(PasswordEncoder passwordEncoder,
                                 @Qualifier(USER_MANAGEMENT_SERVICE) UserManagementService userManagementService,
                                 @Qualifier(BeanName.ROLE_DAL_BEAN) RoleServiceHelper roleServiceHelper,
                                 AuthenticationProvider authenticationProvider)
    {
        m_passwordEncoder = passwordEncoder;
        m_userManagementService = userManagementService;
        m_roleServiceHelper = roleServiceHelper;
        m_authenticationProvider = authenticationProvider;
    }


    public AuthenticationResponse register(RegisterRequest request) throws DataServiceException
    {
        var dto = new UserSignUpRequestDTO(request.getEmail(),
                request.getFirstName(),
                request.getMiddleName(),
                request.getLastName(),
                request.getUsername(),
                m_passwordEncoder.encode(request.getPassword()),
                request.getBirthDate(),
                RoleEnum.ROLE_USER);

        var user = m_userManagementService.saveUser(dto);

        return new AuthenticationResponse(user.getToken(), user.getRefreshToken());
    }

    public boolean verifyTokenByTokenStr(String token, String username)
    {
        return JwtUtil.verifyWithUsernameAndToken(token, username);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        m_authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_userManagementService.findUserByUsername(request.username());
        if (user.getObject() == null)
            return new AuthenticationResponse(null, null);

        var authorities = JwtUtil.populateAuthorities(user.getObject().getRoles());
        var claims = new HashMap<String, Object>();
        claims.put("authorities", authorities);
        System.out.println(authorities);
        var jwtToken = JwtUtil.generateToken(claims, user.getObject().getUsername());
        var refreshToken = JwtUtil.generateRefreshToken(claims, user.getObject().getUsername());

        return new AuthenticationResponse(jwtToken, refreshToken);
    }


    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        refreshToken = authHeader.substring(7);
        username = JwtUtil.extractUsername(refreshToken);
        if (username != null)
        {
            var user = m_userManagementService.findUserByUsername(username);

            if (JwtUtil.isTokenValid(refreshToken, user.getObject().getUsername()))
            {
                var accessToken = JwtUtil.generateToken(user.getObject().getUsername());

                var authResponse = new AuthenticationResponse(accessToken, refreshToken);

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String extractUsername(String token)
    {
        return JwtUtil.extractUsername(token);
    }

}