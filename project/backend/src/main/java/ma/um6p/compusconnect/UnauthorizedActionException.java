
package ma.um6p.compusconnect;
/*
 * Custom exception used when an authenticated user
 * tries to perform an action they are not allowed to perform.
 */
public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}

