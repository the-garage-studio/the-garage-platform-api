package the.garage.platform.iam.application.commands;

/**
 * Command to reactivate a previously suspended user account.
 */
public record ReactivateUserCommand(Long userId) {
    public ReactivateUserCommand {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId cannot be null or less than 1");
    }
}