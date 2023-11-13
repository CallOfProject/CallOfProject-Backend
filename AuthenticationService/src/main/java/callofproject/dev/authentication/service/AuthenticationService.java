package callofproject.dev.authentication.service;

import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import callofproject.dev.service.jwt.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static callofproject.dev.authentication.util.Util.AUTHENTICATION_SERVICE;
import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;

@Service(AUTHENTICATION_SERVICE)
@Lazy
public class AuthenticationService
{
    private final PasswordEncoder m_passwordEncoder;
    private final UserManagementService m_userManagementService;
    private final AuthenticationProvider m_authenticationProvider;


    public AuthenticationService(@Qualifier(USER_MANAGEMENT_SERVICE) UserManagementService userManagementService,
                                 AuthenticationProvider authenticationProvider,
                                 PasswordEncoder passwordEncoder)
    {
        m_passwordEncoder = passwordEncoder;
        m_userManagementService = userManagementService;
        m_authenticationProvider = authenticationProvider;
    }


    public AuthenticationResponse register(RegisterRequest request) throws DataServiceException
    {
        try
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

            return new AuthenticationResponse(user.accessToken(), user.refreshToken(), true);
        } catch (Exception ex)
        {
            throw new DataServiceException(ex.getMessage());
        }
    }


    public Iterable<User> register(List<RegisterRequest> requests) throws DataServiceException
    {
        var list = new ArrayList<UserSignUpRequestDTO>();
        for (var request : requests)
        {
            var dto = new UserSignUpRequestDTO(request.getEmail(),
                    request.getFirstName(),
                    request.getMiddleName(),
                    request.getLastName(),
                    request.getUsername(),
                    m_passwordEncoder.encode(request.getPassword()),
                    request.getBirthDate(),
                    RoleEnum.ROLE_USER);

            list.add(dto);
        }

        return m_userManagementService.saveUsers(list);
    }

    public boolean verifyTokenByTokenStr(String token, String username)
    {
        return JwtUtil.verifyWithUsernameAndToken(token, username);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        m_authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_userManagementService.findUserByUsernameForAuthenticationService(request.username());
        if (user.getObject() == null)
            return new AuthenticationResponse(null, null, false);

        var authorities = JwtUtil.populateAuthorities(user.getObject().getRoles());
        var claims = new HashMap<String, Object>();
        claims.put("authorities", authorities);
        var jwtToken = JwtUtil.generateToken(claims, user.getObject().getUsername());
        var refreshToken = JwtUtil.generateRefreshToken(claims, user.getObject().getUsername());

        return new AuthenticationResponse(jwtToken, refreshToken, true);
    }


    public boolean refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return false;

        refreshToken = authHeader.substring(7);
        username = JwtUtil.extractUsername(refreshToken);
        if (username != null)
        {
            var user = m_userManagementService.findUserByUsernameForAuthenticationService(username);

            if (JwtUtil.isTokenValid(refreshToken, user.getObject().getUsername()))
            {
                var accessToken = JwtUtil.generateToken(user.getObject().getUsername());

                var authResponse = new AuthenticationResponse(accessToken, refreshToken, true);

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return true;
            }
        }
        return false;
    }

    public String extractUsername(String token)
    {
        return JwtUtil.extractUsername(token);
    }


    public boolean validateToken(String token)
    {
        try
        {
            var username = extractUsername(token);

            if (username == null || username.isBlank() || username.isEmpty())
                throw new DataServiceException("User not found!");

            var user = m_userManagementService.findUserByUsername(username);

            return JwtUtil.isTokenValid(token, user.username());

        } catch (DataServiceException ex)
        {
            return false;
        }
    }
}