
package ma.um6p.compusconnect;
/*
 * Custom exception used when a requested resource
 * cannot be found in the database.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

