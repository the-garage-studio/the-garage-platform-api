package the.garage.platform.iam.domain.model.valueobjects;

/**
 * Password value object.
 *
 * <p>Wraps only the <b>encoded</b> representation of a password (RN-02's
 * minimum requirements are enforced at the application layer, before the
 * raw password ever reaches this type). The aggregate never sees or stores
 * a raw password.</p>
 */
public record Password(String hash) {

    public Password {
        if (hash == null || hash.isBlank()) {
            throw new IllegalArgumentException("Password hash cannot be null or blank");
        }
    }

    public static Password ofHash(String hash) {
        return new Password(hash);
    }
}