package callofproject.dev.authentication.service;

import callofproject.dev.library.exception.service.DataServiceException;
import callofproject.dev.repository.authentication.dal.TokenServiceHelper;
import callofproject.dev.repository.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.repository.authentication.entity.security.Token;
import callofproject.dev.authentication.config.JwtService;
import callofproject.dev.authentication.entity.AuthenticationRequest;
import callofproject.dev.authentication.entity.AuthenticationResponse;
import callofproject.dev.authentication.entity.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static callofproject.dev.authentication.util.Util.AUTHENTICATION_SERVICE;
import static callofproject.dev.authentication.util.Util.USER_MANAGEMENT_SERVICE;
import static callofproject.dev.repository.authentication.BeanName.TOKEN_DAL_BEAN;

@Service(AUTHENTICATION_SERVICE)
@Lazy
public class AuthenticationService
{

    private final TokenServiceHelper m_tokenServiceHelper;
    private final PasswordEncoder m_passwordEncoder;
    private final UserManagementService m_userManagementService;
    private final JwtService m_jwtService;
    private AuthenticationManager authenticationManager;

    public AuthenticationService(PasswordEncoder passwordEncoder,
                                 @Qualifier(TOKEN_DAL_BEAN) TokenServiceHelper tokenServiceHelper,
                                 @Qualifier(USER_MANAGEMENT_SERVICE) UserManagementService userManagementService, JwtService jwtService)
    {
        m_tokenServiceHelper = tokenServiceHelper;
        m_passwordEncoder = passwordEncoder;
        m_userManagementService = userManagementService;
        m_jwtService = jwtService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager)
    {
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse register(RegisterRequest request) throws DataServiceException
    {
        var dto = new UserSignUpRequestDTO(request.getEmail(), request.getFirstName(), request.getMiddleName(),
                request.getLastName(), request.getUsername(), m_passwordEncoder.encode(request.getPassword()), request.getBirthDate());
        var user = m_userManagementService.saveUser(dto);


        var jwtToken = m_jwtService.generateToken(user.getObject());
        var refreshToken = m_jwtService.generateRefreshToken(user.getObject());

        saveUserToken(user.getObject().getUsername(), jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    public boolean verifyWithUsernameAndToken(String token, String username)
    {
        return m_jwtService.verifyWithUsernameAndToken(token, username);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
       /* var k = authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        if (!k.isAuthenticated())
            return new AuthenticationResponse(null, null);*/
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_userManagementService.findUserByUsername(request.username());
        if (user.getObject() == null)
            return new AuthenticationResponse(null, null);
        var jwtToken = m_jwtService.generateToken(user.getObject());
        var refreshToken = m_jwtService.generateRefreshToken(user.getObject());
        revokeAllUserTokens(user.getObject().getUsername());
        saveUserToken(user.getObject().getUsername(), jwtToken);
        return new AuthenticationResponse(jwtToken, refreshToken);
    }

    private void saveUserToken(String userId, String jwtToken)
    {
        var token = new Token(userId, jwtToken, false, false);
        m_tokenServiceHelper.saveToken(token);
    }

    private void revokeAllUserTokens(String username)
    {
        var validUserTokens = m_tokenServiceHelper.findAllValidTokenByUsername(username);
        if (validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });

        m_tokenServiceHelper.saveAll(validUserTokens);
    }

    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer "))
            return;

        refreshToken = authHeader.substring(7);
        username = m_jwtService.extractUsername(refreshToken);
        if (username != null)
        {
            var user = m_userManagementService.findUserByUsername(username);

            if (m_jwtService.isTokenValid(refreshToken, user.getObject()))
            {
                var accessToken = m_jwtService.generateToken(user.getObject());
                revokeAllUserTokens(user.getObject().getUsername());
                saveUserToken(user.getObject().getUsername(), accessToken);
                var authResponse = new AuthenticationResponse(accessToken, refreshToken);


                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

}