package ma.um6p.compusconnect;

import jakarta.servlet.DispatcherType;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;
    private final StudentRepository studentRepository;

    // Constructor injection
    public SecurityConfig(
            JwtService jwtService,
            StudentRepository studentRepository) {

        this.jwtService = jwtService;
        this.studentRepository = studentRepository;
    }

    // Password encryption
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        // Create the JWT authentication filter
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(
                        jwtService,
                        studentRepository
                );

        http

                // Disable CSRF for the REST API
                .csrf(csrf -> csrf.disable())

                // Use JWT instead of server-side sessions
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Configure endpoint permissions
                .authorizeHttpRequests(auth -> auth

                        // Allow error dispatches
                        .dispatcherTypeMatchers(
                                DispatcherType.ERROR
                        ).permitAll()

                        // Registration and login are public
                        .requestMatchers("/api/auth/**")
                        .permitAll()

                        // Only administrators can access admin endpoints
                        .requestMatchers("/api/admin/**")
                        .hasRole("ADMIN")

                        // Everyone can read campus information
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/campus-info/**"
                        ).permitAll()

                        // Only administrators can modify campus information
                        .requestMatchers("/api/campus-info/**")
                        .hasRole("ADMIN")

                        // All other endpoints require authentication
                        .anyRequest().authenticated()
                )

                // Execute the JWT filter before the authentication filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}