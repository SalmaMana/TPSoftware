
package ma.um6p.compusconnect;

// Represents an HTTP response status.
import org.springframework.http.HttpStatus;
// Allows us to customize the HTTP response.
import org.springframework.http.ResponseEntity;
// Allows this class to handle exceptions globally.
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
/*
 * Centralized exception handler for the application.
 * It catches specific exceptions and converts them
 * into appropriate HTTP responses.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Handles requests for resources that do not exist.
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<String> handleResourceNotFound(
            ResourceNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(exception.getMessage());
    }

    // Handles actions that an authenticated user is not allowed to perform.
    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<String> handleUnauthorizedAction(
            UnauthorizedActionException exception) {

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(exception.getMessage());
    }
}
