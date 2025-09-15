package com.bank_api.apiGateway.config;

import com.bank_api.apiGateway.filter.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
    
    @Autowired
    private JwtAuthenticationFilter jwtFilter;
    
    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("auth-service", r -> r.path("/api/auth/**")
                        .uri("lb://user-service"))
                
                .route("user-service", r -> r.path("/api/users/**")
                        .filters(f -> f.filter(jwtFilter.createFilter()))
                        .uri("lb://user-service"))
                
                .route("account-service", r -> r.path("/api/accounts/**")
                        .filters(f -> f.filter(jwtFilter.createFilter()))
                        .uri("lb://account-service"))
                
                .route("transfer-service", r -> r.path("/api/transfers/**")
                        .filters(f -> f.filter(jwtFilter.createFilter()))
                        .uri("lb://transfer-service"))
                

                
                
                .route("health-check", r -> r.path("/actuator/health")
                        .uri("lb://api-gateway"))
                .build();
    }
}