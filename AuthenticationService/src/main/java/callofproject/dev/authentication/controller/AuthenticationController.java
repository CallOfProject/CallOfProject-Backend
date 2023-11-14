package callofproject.dev.authentication.controller;


import callofproject.dev.authentication.dto.ErrorMessage;
import callofproject.dev.authentication.dto.auth.AuthenticationRequest;
import callofproject.dev.authentication.dto.auth.RegisterRequest;
import callofproject.dev.authentication.service.AuthenticationService;
import callofproject.dev.authentication.util.Util;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static callofproject.dev.library.exception.util.ExceptionUtil.subscribe;
import static org.springframework.http.ResponseEntity.internalServerError;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("api/auth")
@SecurityRequirement(name = "Authorization")
public class AuthenticationController
{
    private final AuthenticationService m_authenticationService;


    public AuthenticationController(@Qualifier(Util.AUTHENTICATION_SERVICE) AuthenticationService service)
    {
        this.m_authenticationService = service;
    }

    /**
     * Register to application.
     *
     * @param request represent the Register information.
     * @return if success AuthenticationResponse else return ErrorMessage.
     */
    @PostMapping("/register")
    public ResponseEntity<Object> register(@Valid @RequestBody RegisterRequest request)
    {
        return subscribe(() -> ok(m_authenticationService.register(request)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Register multiple user to application.
     *
     * @param request represent the Register information.
     * @return if success AuthenticationResponse else return ErrorMessage.
     */
    @PostMapping("/register-all")
    public ResponseEntity<Object> register(@RequestBody List<RegisterRequest> request)
    {
        return subscribe(() -> ok(m_authenticationService.register(request)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Login operation for application.
     *
     * @param request represent the login information.
     * @return if success returns AuthenticationResponse that include token and status else return ErrorMessage.
     */
    @PostMapping("/login")
    public ResponseEntity<Object> authenticate(@RequestBody AuthenticationRequest request)
    {
        return subscribe(() -> ok(m_authenticationService.authenticate(request)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }


    /**
     * Refresh token
     *
     * @param request  from Servlet
     * @param response from Servlet
     * @return success or not. Return type is boolean.
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response)
    {
        return subscribe(() -> ok(m_authenticationService.refreshToken(request, response)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }

    /**
     * Validate the token with given token.
     *
     * @param token represent the request token
     * @return success or not. Return type is boolean.
     */
    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestParam("token") String token)
    {
        return subscribe(() -> ok(m_authenticationService.validateToken(token)),
                msg -> internalServerError().body(new ErrorMessage(msg.getMessage(), false, 500)));
    }
}