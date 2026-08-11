package the.garage.platform.iam.application.commands;

import the.garage.platform.iam.domain.model.valueobjects.RoleType;

/**
 * Command to change a user's role (RN-43: enforcement of "who can call this"
 * happens at the security-filter level via {@code hasRole("ADMIN")}, not here).
 */
public record ChangeUserRoleCommand(Long userId, RoleType newRole) {
    public ChangeUserRoleCommand {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId cannot be null or less than 1");
        if (newRole == null) throw new IllegalArgumentException("newRole cannot be null");
    }
}