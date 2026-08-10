package the.garage.platform.iam.application.commands;

/**
 * Command to confirm a password reset using a previously issued token.
 */
public record ConfirmPasswordResetCommand(String token, String newRawPassword) {
    public ConfirmPasswordResetCommand {
        if (token == null || token.isBlank()) throw new IllegalArgumentException("token cannot be null or blank");
        if (newRawPassword == null || newRawPassword.isBlank()) throw new IllegalArgumentException("newRawPassword cannot be null or blank");
    }
}