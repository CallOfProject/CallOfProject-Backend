package callofproject.dev.authentication.controller;


import callofproject.dev.authentication.entity.AuthenticationRequest;
import callofproject.dev.authentication.entity.AuthenticationResponse;
import callofproject.dev.authentication.entity.RegisterRequest;
import callofproject.dev.authentication.service.AuthenticationService;
import callofproject.dev.library.exception.service.DataServiceException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.hc.core5.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/auth")
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
        try
        {
            return ResponseEntity.ok(service.register(request));
        } catch (DataServiceException ignored)
        {

            return new ResponseEntity<>(new AuthenticationResponse(null, null),
                    HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request)
    {
        try
        {
            return ResponseEntity.ok(service.authenticate(request));
        } catch (DataServiceException ignored)
        {

            return new ResponseEntity<>(new AuthenticationResponse(null, null),
                    HttpStatusCode.valueOf(HttpStatus.SC_INTERNAL_SERVER_ERROR));
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Object> refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            service.refreshToken(request, response);
            return ResponseEntity.ok(true);
        } catch (DataServiceException ignored)
        {
            return ResponseEntity.badRequest().body(false);
        }
    }


    @GetMapping("/validate")
    public ResponseEntity<Object> validateToken(@RequestParam("uname") String username,
                                                @RequestParam("token") String token)
    {
        try
        {
            return ResponseEntity.ok(service.verifyWithUsernameAndToken(username, token));
        } catch (DataServiceException ignored)
        {
            return ResponseEntity.badRequest().body(false);
        }
    }
}