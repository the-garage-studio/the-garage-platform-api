package the.garage.platform.iam.domain.exceptions;

public class UsernameAlreadyRegisteredException extends RuntimeException {
    public UsernameAlreadyRegisteredException(String username) {
        super("Username '%s' is already taken".formatted(username));
    }
}