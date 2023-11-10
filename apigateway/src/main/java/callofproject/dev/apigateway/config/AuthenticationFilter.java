package callofproject.dev.apigateway.config;


import callofproject.dev.apigateway.service.TokenValidateService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter
{
    private final String authenticationRequest = "/api/auth/";
    private final TokenValidateService m_validateService;

    public AuthenticationFilter(TokenValidateService validateService)
    {
        m_validateService = validateService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        var requestPath = exchange.getRequest().getPath().value();

        if (!requestPath.startsWith(authenticationRequest))
        {
            var request = exchange.getRequest();
            var authorizationHeader = request.getHeaders().getFirst("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

<<<<<<< Updated upstream
            var token = authorizationHeader.substring(7).trim();
            System.out.println("token is: " + token);
            if (!m_validateService.verifyToken(token))
            {
                System.out.println("no");
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
=======
            String token = authorizationHeader.substring(7);

>>>>>>> Stashed changes
            var modifiedRequest = request.mutate()
                    .header("Authorization", "Bearer " + token)
                    .build();
            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        return chain.filter(exchange);
    }
}