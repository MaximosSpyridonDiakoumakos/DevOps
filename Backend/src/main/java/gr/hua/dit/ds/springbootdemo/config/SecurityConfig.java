package gr.hua.dit.ds.springbootdemo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity(debug = true)
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
  
    // Bean for JWT authentication filter
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    // Bean for authentication manager
    @Bean
    public AuthenticationManager authenticationManagerBean (AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    // Main security filter chain configuration
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure CORS
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));

        http
                // Disable CSRF protection
                .csrf(csrf -> csrf.disable())
                // Configure CORS
                .cors(cors -> cors.configurationSource(request -> corsConfiguration))
                // Add JWT authentication filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                // Configure exception handling
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("/api/auth/**", "/actuator/health/**").permitAll()
                        // Swagger UI endpoints (for API documentation) 
                        .requestMatchers("/v3/api-docs/**",
                                "/v2/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html"
                        ).permitAll()
                        // Role-specific endpoints
                        .requestMatchers("/api/projects/pending").hasRole("ADMIN")
                        .requestMatchers("/api/projects").hasRole("CREATOR")
                        .requestMatchers("/api/pledges/**").hasRole("SUPPORTER")
                        .requestMatchers("/creator/**").hasRole("CREATOR")
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // All other requests need authentication
                        .anyRequest().authenticated()
                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
