package the.garage.platform.iam.application.commands;

/**
 * Command to verify a newly registered account's email address.
 */
public record VerifyEmailCommand(String token) {
    public VerifyEmailCommand {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token cannot be null or blank");
    }
}