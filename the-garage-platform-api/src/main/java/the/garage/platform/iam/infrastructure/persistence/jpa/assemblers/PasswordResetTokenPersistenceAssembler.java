package the.garage.platform.iam.infrastructure.persistence.jpa.assemblers;

import the.garage.platform.iam.domain.model.entities.PasswordResetToken;
import the.garage.platform.iam.infrastructure.persistence.jpa.entities.PasswordResetTokenPersistenceEntity;

public final class PasswordResetTokenPersistenceAssembler {

    private PasswordResetTokenPersistenceAssembler() {
    }

    public static PasswordResetToken toDomainFromPersistence(PasswordResetTokenPersistenceEntity entity) {
        if (entity == null) return null;
        return PasswordResetToken.reconstitute(
                entity.getId(), entity.getUserId(), entity.getToken(), entity.getExpiresAt(), entity.isUsed());
    }

    public static PasswordResetTokenPersistenceEntity toPersistenceFromDomain(PasswordResetToken token) {
        if (token == null) return null;
        var entity = new PasswordResetTokenPersistenceEntity();
        if (token.getId() != null) {
            entity.setId(token.getId());
        }
        entity.setUserId(token.getUserId());
        entity.setToken(token.getToken());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setUsed(token.isUsed());
        return entity;
    }
}