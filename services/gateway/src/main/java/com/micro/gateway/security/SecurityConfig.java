package com.micro.gateway.security;

// Spring + Security imports
import org.springframework.context.annotation.Bean;                // Marks security config method as a Bean
import org.springframework.context.annotation.Configuration;      // Marks the class as a configuration source
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity; // Enables reactive security for WebFlux
import org.springframework.security.config.web.server.ServerHttpSecurity; // Fluent API for configuring server security
import org.springframework.security.web.server.SecurityWebFilterChain;    // The security filter chain applied to requests

import static org.springframework.security.config.Customizer.withDefaults; // Utility for default config

@Configuration
@EnableWebFluxSecurity  // Enables WebFlux security capabilities (for reactive stack, not Spring MVC)
public class SecurityConfig {

    /**
     * Main security configuration for the Gateway.
     *
     * - Disables CSRF protection (not needed for stateless APIs)
     * - Allows unauthenticated access to /eureka/** (used for service discovery; don't block registry)
     * - Requires authentication for any other HTTP exchanges
     * - Sets up the server as an OAuth2 resource server (uses JWT tokens for authentication)
     *
     * @param http the fluent ServerHttpSecurity configuration API
     * @return the configured SecurityWebFilterChain for Spring Gateway
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable) // Disables CSRF protection (stateless API is safe)
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/eureka/**").permitAll() // Lets anyone access the Eureka registry
                        .anyExchange().authenticated()          // All other endpoints require authentication
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(withDefaults())                    // Configures JWT validation for OAuth2 tokens
                );
        return http.build(); // Returns built filter chain to Spring
    }
}
