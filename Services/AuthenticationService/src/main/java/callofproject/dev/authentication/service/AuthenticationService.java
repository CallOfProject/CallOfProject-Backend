package callofproject.dev.authentication.service;

import callofproject.dev.authentication.config.kafka.KafkaProducer;
import callofproject.dev.authentication.dto.EmailTopic;
import callofproject.dev.authentication.dto.EmailType;
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
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

@Service(AUTHENTICATION_SERVICE)
@Lazy
public class AuthenticationService
{
    private final UserManagementService m_userManagementService;
    private final PasswordEncoder m_passwordEncoder;
    private final AuthenticationProvider m_authenticationProvider;
    private final KafkaProducer m_kafkaProducer;


    public AuthenticationService(@Qualifier(USER_MANAGEMENT_SERVICE) UserManagementService userManagementService,
                                 AuthenticationProvider authenticationProvider,
                                 PasswordEncoder passwordEncoder, KafkaProducer kafkaProducer)
    {
        m_passwordEncoder = passwordEncoder;
        m_userManagementService = userManagementService;
        m_authenticationProvider = authenticationProvider;
        m_kafkaProducer = kafkaProducer;
    }

    /**
     * Register user with given RegisterRequest parameter.
     *
     * @param request represent the request.
     * @return AuthenticationResponse.
     */
    public AuthenticationResponse register(RegisterRequest request)
    {
        return doForDataService(() -> registerUserCallback(request), "AuthenticationService::register");
    }

    /**
     * Login operation for users.
     *
     * @param request represent the AuthenticationRequest
     * @return the AuthenticationResponse
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        return doForDataService(() -> authenticateCallback(request), "AuthenticationService::authenticate");
    }

    /**
     * Validate given token.
     *
     * @param token represent the jwt.
     * @return boolean value.
     */
    public boolean validateToken(String token)
    {
        return doForDataService(() -> validateTokenCallback(token), "AuthenticationService::validateToken");
    }

    //-------------------------------------------------CALLBACK-----------------------------------------------------

    /**
     * Register user with given RegisterRequest parameter.
     *
     * @param request represent the request.
     * @return AuthenticationResponse.
     */
    private AuthenticationResponse registerUserCallback(RegisterRequest request)
    {
        var dto = new UserSignUpRequestDTO(request.getEmail(), request.getFirst_name(),
                request.getMiddle_name(), request.getLast_name(), request.getUsername(),
                m_passwordEncoder.encode(request.getPassword()), request.getBirth_date(),
                RoleEnum.ROLE_USER);

        var user = m_userManagementService.saveUser(dto);

        m_kafkaProducer.sedVerificationEmail(new EmailTopic(EmailType.EMAIL_VERIFICATION,
                "canozturk309@gmail.com"));
        return new AuthenticationResponse(user.accessToken(), user.refreshToken(), true, RoleEnum.ROLE_USER.getRole(), user.userId());
    }

    /**
     * Register all users with given RegisterRequest list parameter.
     *
     * @param requests represent the RegisterRequest list.
     * @return Iterable<User>
     */
    public Iterable<User> registerAll(List<RegisterRequest> requests)
    {
        var list = new ArrayList<UserSignUpRequestDTO>();
        for (var request : requests)
        {
            var dto = new UserSignUpRequestDTO(request.getEmail(),
                    request.getFirst_name(),
                    request.getMiddle_name(),
                    request.getLast_name(),
                    request.getUsername(),
                    m_passwordEncoder.encode(request.getPassword()),
                    request.getBirth_date(),
                    RoleEnum.ROLE_USER);

            list.add(dto);
        }

        return m_userManagementService.saveUsers(list);
    }

    /**
     * Login operation for admins.
     *
     * @param request represent the login information.
     * @return if success returns AuthenticationResponse that include token and status else return ErrorMessage.
     */
    private AuthenticationResponse authenticateCallback(AuthenticationRequest request)
    {
        m_authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.username(), request.password()));
        var user = m_userManagementService.findUserByUsernameForAuthenticationService(request.username());

        if (user.getObject() == null)
            return new AuthenticationResponse(null, null, false, null, false,
                    null);

        if (user.getObject().getIsAccountBlocked())
            return new AuthenticationResponse(false, true);

        var topRole = findTopRole(user.getObject());
        var authorities = JwtUtil.populateAuthorities(user.getObject().getRoles());
        var claims = new HashMap<String, Object>();

        claims.put("authorities", authorities);

        var jwtToken = JwtUtil.generateToken(claims, user.getObject().getUsername());
        var refreshToken = JwtUtil.generateRefreshToken(claims, user.getObject().getUsername());

        return new AuthenticationResponse(jwtToken, refreshToken, true, topRole, false,
                user.getObject().getUserId());
    }

    /**
     * Find top role for user.
     *
     * @param user represent the user.
     * @return String value.
     */
    private String findTopRole(User user)
    {
        var role = RoleEnum.ROLE_USER.getRole();

        for (var r : user.getRoles())
        {
            if (r.getName().equals(RoleEnum.ROLE_ROOT.getRole()))
            {
                role = RoleEnum.ROLE_ROOT.getRole();
                break;
            }
            if (r.getName().equals(RoleEnum.ROLE_ADMIN.getRole()))
                role = RoleEnum.ROLE_ADMIN.getRole();
        }
        return role;
    }

    /**
     * Refresh token.
     *
     * @param request  represent the request.
     * @param response represent the response.
     * @return boolean value.
     * @throws IOException if an I/O error occurs.
     */
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
                var topRole = findTopRole(user.getObject());
                var authResponse = new AuthenticationResponse(accessToken, refreshToken, true, topRole,
                        user.getObject().getIsAccountBlocked(), user.getObject().getUserId());

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
                return true;
            }
        }
        return false;
    }

    /**
     * Validate given token.
     *
     * @param token represent the jwt.
     * @return boolean value.
     */
    private boolean validateTokenCallback(String token)
    {
        var username = JwtUtil.extractUsername(token);

        if (username == null || username.isBlank() || username.isEmpty())
            throw new DataServiceException("User not found!");

        var user = m_userManagementService.findUserByUsername(username);

        return JwtUtil.isTokenValid(token, user.username());
    }
}