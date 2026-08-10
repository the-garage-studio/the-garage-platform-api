package the.garage.platform.iam.infrastructure.persistence.jpa.entities;

import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.domain.model.valueobjects.RoleType;
import the.garage.platform.iam.domain.model.valueobjects.UserStatus;
import the.garage.platform.iam.infrastructure.persistence.jpa.converters.EmailPersistenceConverter;
import the.garage.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class UserPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Convert(converter = EmailPersistenceConverter.class)
    @Column(nullable = false, unique = true, length = 150)
    private Email email;

    @Column(name = "password_hash", nullable = false, length = 120)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleType role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private UserStatus status;
}
