package com.bank_api.apiGateway.filter;

import com.bank_api.apiGateway.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> {
    
    @Autowired
    private JwtUtil jwtUtil;
    
    public JwtAuthenticationFilter() {
        super(Config.class);
    }
    
    
    public GatewayFilter createFilter() {
        return apply(new Config());
    }
    
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            
            // Skip authentication for auth endpoints
            if (request.getURI().getPath().contains("/api/auth/")) {
                return chain.filter(exchange);
            }
            
            // Check for authorization header
            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            }
            
            String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                
                try {
                    // Validate JWT token
                    if (jwtUtil.validateToken(token)) {
                        // Add user ID to request headers for downstream services
                        Long userId = jwtUtil.extractUserId(token);
                        ServerHttpRequest mutatedRequest = request.mutate()
                                .header("X-User-ID", userId.toString())
                                .build();
                        
                        ServerWebExchange mutatedExchange = exchange.mutate()
                                .request(mutatedRequest)
                                .build();
                        
                        return chain.filter(mutatedExchange);
                    }
                } catch (Exception e) {
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }
            }
            
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        };
    }
    
    public static class Config {
        // Configuration properties - can be empty for now
    }
}