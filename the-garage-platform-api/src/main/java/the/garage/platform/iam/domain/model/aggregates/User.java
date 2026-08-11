package the.garage.platform.iam.domain.model.aggregates;

import lombok.Getter;
import lombok.Setter;
import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.domain.model.valueobjects.Password;
import the.garage.platform.iam.domain.model.valueobjects.RoleType;
import the.garage.platform.iam.domain.model.valueobjects.UserStatus;
import the.garage.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

@Getter
public class User extends AbstractDomainAggregateRoot<User> {
    @Setter
    private Long id;

    private String firstName;
    private String lastName;
    private String username;
    private Email email;
    private Password password;
    private RoleType role;
    private UserStatus status;

    protected User() {}

    private User(String firstName, String lastName, String username, Email email,
                 Password password, RoleType role, UserStatus status) {
        setFirstName(firstName);
        setLastName(lastName);
        setUsername(username);
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    /**
     * Factory method used by the application layer for public self-registration.
     * Always assigns {@link RoleType#COLLECTOR} and starts as
     * {@link UserStatus#PENDING_VERIFICATION} (RN-06) — nobody registers as ADMIN.
     */
    public static User register(String firstName, String lastName, String username, Email email, Password password) {
        return new User(firstName, lastName, username, email, password, RoleType.COLLECTOR, UserStatus.PENDING_VERIFICATION);
    }

    /**
     * Factory method reserved for the deployment seed script. Never invoked
     * from a REST endpoint or public application service.
     */
    public static User seedAdministrator(String firstName, String lastName, String username, Email email, Password password) {
        return new User(firstName, lastName, username, email, password, RoleType.ADMIN, UserStatus.ACTIVE);
    }

    /**
     * Reconstructs a User from persisted state. Used exclusively by the
     * infrastructure assembler layer — never by application services.
     */
    public static User reconstitute(Long id, String firstName, String lastName, String username,
                                    Email email, Password password, RoleType role, UserStatus status) {
        var user = new User(firstName, lastName, username, email, password, role, status);
        user.id = id;
        return user;
    }

    private void setFirstName(String firstName) {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("firstName cannot be null or blank");
        this.firstName = firstName;
    }

    private void setLastName(String lastName) {
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("lastName cannot be null or blank");
        this.lastName = lastName;
    }

    private void setUsername(String username) {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username cannot be null or blank");
        this.username = username.trim();
    }

    /** Transitions PENDING_VERIFICATION -> ACTIVE after email verification succeeds. */
    public void activate() {
        if (status != UserStatus.PENDING_VERIFICATION) {
            throw new IllegalStateException("Only a pending-verification account can be activated");
        }
        this.status = UserStatus.ACTIVE;
    }

    /** RN-07: deactivates the account; historical data is preserved. */
    public void suspend() {
        if (status == UserStatus.SUSPENDED) {
            throw new IllegalStateException("Account is already suspended");
        }
        this.status = UserStatus.SUSPENDED;
    }

    public void reactivate() {
        if (status != UserStatus.SUSPENDED) {
            throw new IllegalStateException("Only a suspended account can be reactivated");
        }
        this.status = UserStatus.ACTIVE;
    }

    /** Who is allowed to call this (RN-43) is enforced at the application layer, not here. */
    public void changeRole(RoleType newRole) {
        if (newRole == null) throw new IllegalArgumentException("newRole cannot be null");
        this.role = newRole;
    }

    public void changePassword(Password newPassword) {
        if (newPassword == null) throw new IllegalArgumentException("newPassword cannot be null");
        this.password = newPassword;
    }

    /** RN-07: a non-active account cannot authenticate. */
    public boolean isActive() {
        return status == UserStatus.ACTIVE;
    }

    public boolean isAdmin() {
        return role == RoleType.ADMIN;
    }
}
