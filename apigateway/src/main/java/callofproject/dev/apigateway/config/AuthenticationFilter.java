package callofproject.dev.apigateway.config;


import callofproject.dev.apigateway.service.TokenValidateService;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthenticationFilter implements GlobalFilter
{
    private final TokenValidateService m_validateService;

    public AuthenticationFilter(TokenValidateService validateService)
    {
        m_validateService = validateService;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain)
    {
        String requestPath = exchange.getRequest().getPath().value();

        if (!requestPath.startsWith("/api/auth/"))
        {
            ServerHttpRequest request = exchange.getRequest();
            String authorizationHeader = request.getHeaders().getFirst("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer "))
            {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }

            String token = authorizationHeader.substring(7);

            // Token'ı kullanmak veya doğrulamak için token değişkenini kullanabilirsiniz

            // Örnek: Token'ı başka bir servise gönderme
            // Bu örnekte token'ı bir HTTP başlığı olarak ekliyoruz
            ServerHttpRequest modifiedRequest = request.mutate()
                    .header("Authorization", "Bearer " + token)
                    .build();

            return chain.filter(exchange.mutate().request(modifiedRequest).build());
        }

        return chain.filter(exchange);
    }
}