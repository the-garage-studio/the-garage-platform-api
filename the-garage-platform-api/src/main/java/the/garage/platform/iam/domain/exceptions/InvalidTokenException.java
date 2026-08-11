package the.garage.platform.iam.domain.exceptions;

public class InvalidTokenException extends RuntimeException {
    public InvalidTokenException(String context) {
        super("Invalid token: %s".formatted(context));
    }
}