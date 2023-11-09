package callofproject.dev.apigateway.service;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Lazy
public class TokenValidateService
{
    private final RestTemplate m_restTemplate;

    public TokenValidateService(RestTemplate restTemplate)
    {
        m_restTemplate = restTemplate;
    }


    public boolean verifyToken(String token)
    {
        var request = String.format("http://localhost:3131/api/auth/validate?token=%s", token);
        return Boolean.TRUE.equals(m_restTemplate.getForObject(request, boolean.class));
    }
}
