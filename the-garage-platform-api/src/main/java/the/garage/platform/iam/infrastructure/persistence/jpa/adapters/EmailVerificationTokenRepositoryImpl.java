package the.garage.platform.iam.infrastructure.persistence.jpa.adapters;
import org.springframework.stereotype.Repository;
import the.garage.platform.iam.domain.model.entities.EmailVerificationToken;
import the.garage.platform.iam.domain.repositories.EmailVerificationTokenRepository;
import the.garage.platform.iam.infrastructure.persistence.jpa.assemblers.EmailVerificationTokenPersistenceAssembler;
import the.garage.platform.iam.infrastructure.persistence.jpa.repositories.EmailVerificationTokenPersistenceRepository;

import java.util.Optional;

@Repository
public class EmailVerificationTokenRepositoryImpl implements EmailVerificationTokenRepository {

    private final EmailVerificationTokenPersistenceRepository persistenceRepository;

    public EmailVerificationTokenRepositoryImpl(EmailVerificationTokenPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<EmailVerificationToken> findByToken(String token) {
        return persistenceRepository.findByToken(token).map(EmailVerificationTokenPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public EmailVerificationToken save(EmailVerificationToken token) {
        var saved = persistenceRepository.save(EmailVerificationTokenPersistenceAssembler.toPersistenceFromDomain(token));
        return EmailVerificationTokenPersistenceAssembler.toDomainFromPersistence(saved);
    }
}