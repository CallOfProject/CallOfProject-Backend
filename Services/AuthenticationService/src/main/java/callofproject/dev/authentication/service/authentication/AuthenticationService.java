package callofproject.dev.authentication.service.authentication;

import callofproject.dev.authentication.dto.UserSignUpRequestDTO;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.User;
import callofproject.dev.repository.authentication.enumeration.RoleEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static callofproject.dev.authentication.util.AuthenticationServiceBeanName.AUTHENTICATION_SERVICE;
import static callofproject.dev.library.exception.util.CopDataUtil.doForDataService;

/**
 * Service class for authentication-related operations.
 * It implements the IAuthenticationService interface.
 */
@Service(AUTHENTICATION_SERVICE)
@Lazy
public class AuthenticationService implements IAuthenticationService
{
    private final PasswordEncoder m_passwordEncoder;
    private final AuthenticationServiceCallback m_serviceCallback;


    /**
     * Constructor for AuthenticationService class.
     *
     * @param passwordEncoder The PasswordEncoder object to be injected.
     * @param serviceCallback The AuthenticationServiceCallback object to be injected.
     */
    public AuthenticationService(PasswordEncoder passwordEncoder, AuthenticationServiceCallback serviceCallback)
    {
        m_passwordEncoder = passwordEncoder;
        m_serviceCallback = serviceCallback;
    }


    /**
     * Register user with given RegisterRequest parameter.
     *
     * @param request represent the request.
     * @return AuthenticationResponse.
     */
    @Override
    public AuthenticationResponse register(RegisterRequest request)
    {
        var result = doForDataService(() -> m_serviceCallback.registerUserCallback(request), "AuthenticationService::register");

        if (result.isSuccess())
            m_serviceCallback.sendAuthenticationEmail(new UserSignUpRequestDTO(request.getEmail(), request.getFirst_name(),
                    request.getMiddle_name(), request.getLast_name(), request.getUsername(),
                    m_passwordEncoder.encode(request.getPassword()), request.getBirth_date(),
                    RoleEnum.ROLE_USER));

        return result;
    }

    /**
     * Login operation for users.
     *
     * @param request represent the AuthenticationRequest
     * @return the AuthenticationResponse
     */
    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request)
    {
        return doForDataService(() -> m_serviceCallback.authenticateCallback(request), "AuthenticationService::authenticate");
    }

    /**
     * Validate given token.
     *
     * @param token represent the jwt.
     * @return boolean value.
     */
    @Override
    public boolean validateToken(String token)
    {
        return doForDataService(() -> m_serviceCallback.validateTokenCallback(token), "AuthenticationService::validateToken");
    }

    /**
     * Refresh token.
     *
     * @param request  represent the request.
     * @param response represent the response.
     * @return boolean value.
     */
    @Override
    public boolean refreshToken(HttpServletRequest request, HttpServletResponse response)
    {
        return doForDataService(() -> m_serviceCallback.refreshTokenCallback(request, response), "AuthenticationService::refreshToken");
    }


    /**
     * Verify user and register.
     *
     * @param token represent the token.
     * @return ResponseMessage
     */
    @Override
    public ResponseMessage<Object> verifyUserAndRegister(String token)
    {
        return doForDataService(() -> m_serviceCallback.verifyUserAndRegisterCallback(token), "AuthenticationService::verifyUserAndRegister");
    }

    /**
     * Find user by username.
     *
     * @param username represent the username.
     * @return User.
     */
    @Override
    public Optional<User> findUserByUsername(String username)
    {
        return m_serviceCallback.findUserByUsernameCallback(username);
    }
}