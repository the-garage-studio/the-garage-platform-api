package the.garage.platform.iam.domain.exceptions;

public class TokenExpiredException extends RuntimeException {
    public TokenExpiredException(String context) {
        super("Token expired: %s".formatted(context));
    }
}