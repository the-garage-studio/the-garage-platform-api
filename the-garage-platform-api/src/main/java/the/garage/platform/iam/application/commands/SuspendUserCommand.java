package the.garage.platform.iam.application.commands;

public record SuspendUserCommand(Long userId) {
    public SuspendUserCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("userId cannot be null or less than 1");
        }
    }
}
