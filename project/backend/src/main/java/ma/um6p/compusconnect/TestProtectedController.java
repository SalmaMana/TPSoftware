package ma.um6p.compusconnect;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestProtectedController {

    // This endpoint is only accessible to authenticated requests
    @GetMapping("/api/test/protected")
    public String protectedEndpoint() {
        return "You are authenticated! Your JWT is valid.";
    }
}
