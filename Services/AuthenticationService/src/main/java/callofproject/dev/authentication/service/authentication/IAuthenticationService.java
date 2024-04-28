package callofproject.dev.authentication.service.authentication;

import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

/**
 * Service class for handling authentication operations.
 */
public interface IAuthenticationService
{
    /**
     * Register a new user.
     *
     * @param request The RegisterRequest object representing the user's registration request.
     * @return The AuthenticationResponse object representing the user's authentication response.
     */
    AuthenticationResponse register(RegisterRequest request);

    /**
     * Register a new user for mobile devices. This method is used for testing.
     *
     * @param request The RegisterRequest object representing the user's registration request.
     * @return The AuthenticationResponse object representing the user's authentication response.
     */
    AuthenticationResponse registerForMobile(RegisterRequest request);

    /**
     * Authenticate a user.
     *
     * @param request The AuthenticationRequest object representing the user's authentication request.
     * @return The AuthenticationResponse object representing the user's authentication response.
     */
    AuthenticationResponse authenticate(AuthenticationRequest request);

    /**
     * Verify a user and register them.
     *
     * @param token The token representing the user's verification.
     * @return The ResponseMessage object representing the response message.
     */
    ResponseMessage<Object> verifyUserAndRegister(String token);

    /**
     * Find a user by their username.
     *
     * @param username The username of the user.
     * @return The User object representing the user.
     */
    Optional<User> findUserByUsername(String username);

    /**
     * Validate a token.
     *
     * @param token The token to validate.
     * @return True if the token is valid, false otherwise.
     */
    boolean validateToken(String token);

    /**
     * Refresh a token.
     *
     * @param request  The HttpServletRequest object representing the request.
     * @param response The HttpServletResponse object representing the response.
     * @return True if the token was refreshed, false otherwise.
     */
    boolean refreshToken(HttpServletRequest request, HttpServletResponse response);
}