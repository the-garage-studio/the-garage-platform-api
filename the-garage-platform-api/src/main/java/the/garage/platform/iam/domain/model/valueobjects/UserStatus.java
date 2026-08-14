package the.garage.platform.iam.domain.model.valueobjects;

/**
 * Lifecycle status of a {@link com.thegarage.platform.identityaccess.domain.model.aggregates.User}.
 *
 * <p>{@code SUSPENDED} accounts are rejected at sign-in (RN-07), but their
 * historical data is preserved rather than deleted.</p>
 */
public enum UserStatus {
    PENDING_VERIFICATION,
    ACTIVE,
    SUSPENDED,
    DELETED
}