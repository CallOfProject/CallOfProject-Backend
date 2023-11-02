package callofproject.dev.apigateway.api.controller;

import callofproject.dev.apigateway.api.authentication.AuthenticationService;
import callofproject.dev.apigateway.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthenticationController
{
    private final AuthenticationService m_authenticationService;

    public AuthenticationController(AuthenticationService authenticationService)
    {
        m_authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity register(@RequestBody RegisterRequest request)
    {
        return m_authenticationService.register(request);
    }

  /*  @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
    {
        return m_authenticationService.login(request);
    }*/
}
