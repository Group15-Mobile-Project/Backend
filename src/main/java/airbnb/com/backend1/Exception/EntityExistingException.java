package airbnb.com.backend1.Exception;

public class EntityExistingException extends RuntimeException {
    
    public EntityExistingException(String message) {
        super(message);
    }
}
