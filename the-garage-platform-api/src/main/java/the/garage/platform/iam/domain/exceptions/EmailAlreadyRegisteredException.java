package the.garage.platform.iam.domain.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String email) {
        super("An account with email '%s' already exists".formatted(email));
    }
}