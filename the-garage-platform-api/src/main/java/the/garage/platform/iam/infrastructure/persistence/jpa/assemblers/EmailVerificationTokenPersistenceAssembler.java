package the.garage.platform.iam.infrastructure.persistence.jpa.assemblers;

import the.garage.platform.iam.domain.model.entities.EmailVerificationToken;
import the.garage.platform.iam.infrastructure.persistence.jpa.entities.EmailVerificationTokenPersistenceEntity;

public final class EmailVerificationTokenPersistenceAssembler {

    private EmailVerificationTokenPersistenceAssembler() {
    }

    public static EmailVerificationToken toDomainFromPersistence(EmailVerificationTokenPersistenceEntity entity) {
        if (entity == null) return null;
        return EmailVerificationToken.reconstitute(
                entity.getId(), entity.getUserId(), entity.getToken(), entity.getExpiresAt(), entity.isVerified());
    }

    public static EmailVerificationTokenPersistenceEntity toPersistenceFromDomain(EmailVerificationToken token) {
        if (token == null) return null;
        var entity = new EmailVerificationTokenPersistenceEntity();
        if (token.getId() != null) {
            entity.setId(token.getId());
        }
        entity.setUserId(token.getUserId());
        entity.setToken(token.getToken());
        entity.setExpiresAt(token.getExpiresAt());
        entity.setVerified(token.isVerified());
        return entity;
    }
}