package the.garage.platform.iam.domain.model.valueobjects;

/**
 * The two roles supported by The Garage (RN-06).
 *
 * <p>{@code ADMIN} is never assigned through the public registration flow —
 * only via the deployment seed script, or granted afterward by an existing
 * administrator (RN-43).</p>
 */
public enum RoleType {
    COLLECTOR,
    ADMIN
}