package the.garage.platform.iam.interfaces.acl;

public interface IdentityAccessContextFacade {

    /**
     * Suspends a user account.
     * @return {@code true} when the operation succeeded; otherwise {@code false}
     */
    boolean suspendUser(Long userId);

    /**
     * Reactivates a previously suspended user account.
     * @return {@code true} when the operation succeeded; otherwise {@code false}
     */
    boolean reactivateUser(Long userId);

    /**
     * Changes a user's role. {@code roleName} must be a valid {@code RoleType} name.
     * @return {@code true} when the operation succeeded; otherwise {@code false}
     */
    boolean changeUserRole(Long userId, String roleName);

    /**
     * Fetches a user's identifier by username.
     * @return user identifier, or {@code 0L} when not found
     */
    Long fetchUserIdByUsername(String username);

    /**
     * Fetches a username by user identifier.
     * @return username, or an empty string when not found
     */
    String fetchUsernameByUserId(Long userId);
}