package the.garage.platform.iam.domain.repositories;

import the.garage.platform.iam.domain.model.entities.PasswordResetToken;

import java.util.Optional;

public interface PasswordResetTokenRepository {
    Optional<PasswordResetToken> findByToken(String token);
    PasswordResetToken save(PasswordResetToken token);
}