package the.garage.platform.iam.domain.repositories;

import the.garage.platform.iam.domain.model.entities.EmailVerificationToken;

import java.util.Optional;

public interface EmailVerificationTokenRepository {
    Optional<EmailVerificationToken> findByToken(String token);
    EmailVerificationToken save(EmailVerificationToken token);
}