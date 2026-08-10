package the.garage.platform.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.garage.platform.iam.infrastructure.persistence.jpa.entities.EmailVerificationTokenPersistenceEntity;

import java.util.Optional;

@Repository
public interface EmailVerificationTokenPersistenceRepository extends JpaRepository<EmailVerificationTokenPersistenceEntity, Long> {
    Optional<EmailVerificationTokenPersistenceEntity> findByToken(String token);
}