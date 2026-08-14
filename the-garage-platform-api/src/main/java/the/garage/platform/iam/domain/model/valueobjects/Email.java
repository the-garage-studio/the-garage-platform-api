package the.garage.platform.iam.domain.model.valueobjects;

import java.util.regex.Pattern;

/**
 * Email value object.
 *
 * <p>Guarantees every {@link Email} instance is well-formed. Uniqueness (RN-01)
 * is enforced at the application layer via
 * {@link com.thegarage.platform.identityaccess.domain.repositories.UserRepository}.</p>
 */
public record Email(String address) {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    public Email {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        address = address.trim().toLowerCase();
        if (!EMAIL_PATTERN.matcher(address).matches()) {
            throw new IllegalArgumentException("Email must be a valid format");
        }
    }
}