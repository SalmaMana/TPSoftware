package ma.um6p.compusconnect;

// JWT creation tools
import io.jsonwebtoken.Jwts;
// Cryptographic key tools
import io.jsonwebtoken.security.Keys;
// Java date tools
import java.util.Date;
// Base64 decoding
import java.util.Base64;

// Spring configuration tools
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// JWT service responsible for generating tokens
@Service
public class JwtService {

    // Signing key used to sign the JWT
    private final javax.crypto.SecretKey signingKey;

    // Token validity duration: 24 hours in milliseconds
    private static final long EXPIRATION_TIME = 86400000;

    // Constructor: reads the secret key from application.properties
    public JwtService(@Value("${jwt.secret}") String secret) {

        // Decode the Base64 secret into bytes
        byte[] keyBytes = Base64.getDecoder().decode(secret);

        // Create a cryptographic key from those bytes
        this.signingKey = Keys.hmacShaKeyFor(keyBytes);
    }

    // Generate a JWT token for a student
    public String generateToken(String email) {

        // Current time
        Date now = new Date();

        // Token expiration time
        Date expiration = new Date(now.getTime() + EXPIRATION_TIME);
        // Build, sign and generate the JWT
        return Jwts.builder()
                // Store the student's email as the subject
                .subject(email)
                // Record when the token was created
                .issuedAt(now)
                // Record when the token expires
                .expiration(expiration)

                // Sign the token using our secret key
                .signWith(signingKey)

                // Produce the final token string
                .compact();
    }
    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
}
