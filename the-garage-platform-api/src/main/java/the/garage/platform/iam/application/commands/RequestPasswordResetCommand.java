package the.garage.platform.iam.application.commands;

/**
 * Command to request a password reset link (US21).
 */
public record RequestPasswordResetCommand(String email) {
    public RequestPasswordResetCommand {
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email cannot be null or blank");
    }
}