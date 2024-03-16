package callofproject.dev.authentication.service.authentication;

import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.AuthenticationResponse;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.data.common.clas.ResponseMessage;
import callofproject.dev.repository.authentication.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Optional;

public interface IAuthenticationService
{
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);

    ResponseMessage<Object> verifyUserAndRegister(String token);

    Optional<User> findUserByUsername(String username);

    boolean validateToken(String token);

    boolean refreshToken(HttpServletRequest request, HttpServletResponse response);

}
