package the.garage.platform.iam.infrastructure.persistence.jpa.assemblers;

import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.iam.domain.model.valueobjects.Password;
import the.garage.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;

/**
 * Static assembler between User domain and persistence representations.
 */
public final class UserPersistenceAssembler {

    private UserPersistenceAssembler() {
    }

    public static User toDomainFromPersistence(UserPersistenceEntity entity) {
        if (entity == null) return null;
        return User.reconstitute(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getUsername(),
                entity.getEmail(),
                Password.ofHash(entity.getPasswordHash()),
                entity.getRole(),
                entity.getStatus());
    }

    public static UserPersistenceEntity toPersistenceFromDomain(User user) {
        if (user == null) return null;
        var entity = new UserPersistenceEntity();
        if (user.getId() != null) {
            entity.setId(user.getId());
        }
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setUsername(user.getUsername());
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPassword().hash());
        entity.setRole(user.getRole());
        entity.setStatus(user.getStatus());
        return entity;
    }
}

