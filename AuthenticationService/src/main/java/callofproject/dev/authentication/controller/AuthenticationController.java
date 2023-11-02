package callofproject.dev.authentication.controller;

import callofproject.dev.authentication.entity.AuthenticationRequest;
import callofproject.dev.authentication.entity.AuthenticationResponse;
import callofproject.dev.authentication.entity.RegisterRequest;
import callofproject.dev.authentication.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("auth")
public class AuthenticationController
{
    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service)
    {
        this.service = service;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request)
    {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        service.refreshToken(request, response);
    }
}