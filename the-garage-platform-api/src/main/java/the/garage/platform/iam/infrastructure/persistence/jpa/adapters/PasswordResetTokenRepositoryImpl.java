package the.garage.platform.iam.infrastructure.persistence.jpa.adapters;

import org.springframework.stereotype.Repository;
import the.garage.platform.iam.domain.model.entities.PasswordResetToken;
import the.garage.platform.iam.domain.repositories.PasswordResetTokenRepository;
import the.garage.platform.iam.infrastructure.persistence.jpa.assemblers.PasswordResetTokenPersistenceAssembler;
import the.garage.platform.iam.infrastructure.persistence.jpa.repositories.PasswordResetTokenPersistenceRepository;

import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryImpl implements PasswordResetTokenRepository {

    private final PasswordResetTokenPersistenceRepository persistenceRepository;

    public PasswordResetTokenRepositoryImpl(PasswordResetTokenPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<PasswordResetToken> findByToken(String token) {
        return persistenceRepository.findByToken(token).map(PasswordResetTokenPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        var saved = persistenceRepository.save(PasswordResetTokenPersistenceAssembler.toPersistenceFromDomain(token));
        return PasswordResetTokenPersistenceAssembler.toDomainFromPersistence(saved);
    }
}