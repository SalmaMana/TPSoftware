package ma.um6p.compusconnect;

// Imports for Spring Security configuration
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Import the security filter chain
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

// Import password encoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

// Allow internal error dispatches
import jakarta.servlet.DispatcherType;

// @Configuration tells Spring this class contains configuration.
@Configuration
public class SecurityConfig {

    // Defines the security rules for HTTP requests.
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        // Configure which requests are allowed.
        http.authorizeHttpRequests(authorize -> authorize

                // Allow internal error dispatches.
                .dispatcherTypeMatchers(DispatcherType.ERROR).permitAll()

                // Registration endpoints are publicly accessible.
                .requestMatchers("/api/auth/**").permitAll()

                // Everything else requires authentication.
                .anyRequest().authenticated()
        );

        // Disable CSRF protection for our initial API testing.
        http.csrf(csrf -> csrf.disable());

        // Return the configured security chain.
        return http.build();
    }

    // Creates the password hashing component.
    // BCrypt is used to hash passwords.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
