package the.garage.platform.iam.domain.exceptions;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(String username) {
        super("Account '%s' is not active".formatted(username));
    }
}