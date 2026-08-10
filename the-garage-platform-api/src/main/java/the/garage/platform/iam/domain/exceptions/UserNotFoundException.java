package the.garage.platform.iam.domain.exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String identifier) {
        super("User not found: %s".formatted(identifier));
    }
}