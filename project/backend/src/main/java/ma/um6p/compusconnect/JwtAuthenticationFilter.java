package ma.um6p.compusconnect;

import io.jsonwebtoken.JwtException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final StudentRepository studentRepository;

    // Constructor injection
    public JwtAuthenticationFilter(
            JwtService jwtService,
            StudentRepository studentRepository) {

        this.jwtService = jwtService;
        this.studentRepository = studentRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Read the Authorization header
        String authHeader = request.getHeader("Authorization");

        // Check whether a Bearer token was provided
        if (authHeader == null ||
                !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT token
        String token = authHeader.substring(7);

        try {

            // Extract the email from the validated token
            String email = jwtService.extractEmail(token);

            // Authenticate only if authentication does not exist
            if (email != null &&
                    SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                // Retrieve the student's current information
                Student student = studentRepository.findByEmail(email);

                // Do not authenticate suspended or nonexistent accounts
                if (student == null || student.isSuspended()) {

                    SecurityContextHolder.clearContext();

                    filterChain.doFilter(request, response);
                    return;
                }

                // Determine the student's role
                String role;

                if (student.isAdministrator()) {
                    role = "ROLE_ADMIN";
                } else {
                    role = "ROLE_STUDENT";
                }

                // Create authentication with the appropriate role
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                List.of(new SimpleGrantedAuthority(role))
                        );

                // Attach request details
                authentication.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Store authentication in Spring Security
                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        } catch (JwtException | IllegalArgumentException e) {

            // Clear authentication if the token is invalid
            SecurityContextHolder.clearContext();
        }

        // Continue processing the request
        filterChain.doFilter(request, response);
    }
}