package callofproject.dev.apigateway.api.authentication;

import callofproject.dev.apigateway.dto.RegisterRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Lazy
public class AuthenticationService
{
    private final RestTemplate m_restTemplate;

    public AuthenticationService(RestTemplate restTemplate)
    {
        m_restTemplate = restTemplate;
    }

    public ResponseEntity register(RegisterRequest request)
    {
        HttpEntity<Product> request = new HttpEntity<Product>(
                new Product("Television", "Samsung",1145.67,"S001"));
        return m_restTemplate.postForObject("http://localhost:9091/auth/register", request,
                ResponseEntity.class);
    }


    /*public ResponseEntity<AuthenticationResponse> login(@RequestBody AuthenticationRequest request)
    {
        return m_authenticationService.login(request);
    }*/
}
