package ma.um6p.compusconnect;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
public class SecurityConfig {

    private final JwtService jwtService;

    // Constructor injection
    public SecurityConfig(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    // BCrypt password encoder bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Configure Spring Security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {

        // Create our JWT filter
        JwtAuthenticationFilter jwtAuthenticationFilter =
                new JwtAuthenticationFilter(jwtService);

        http
                // Disable CSRF for our REST API (initial API configuration)
                .csrf(csrf -> csrf.disable())

                // We don't use server-side sessions for JWT authentication
                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Define which requests are accessible
                .authorizeHttpRequests(auth -> auth
                        .dispatcherTypeMatchers(
                                jakarta.servlet.DispatcherType.ERROR
                        ).permitAll()

                                .requestMatchers("/api/auth/**").permitAll()
                               // Allow reading campus information without a JWT token
                                .requestMatchers("/api/campus-info/**").permitAll()
                                .anyRequest().authenticated()
                )

                // Add JWT filter before the username/password filter
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}