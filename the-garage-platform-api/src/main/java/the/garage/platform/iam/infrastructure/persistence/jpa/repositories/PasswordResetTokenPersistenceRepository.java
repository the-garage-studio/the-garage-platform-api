package the.garage.platform.iam.infrastructure.persistence.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import the.garage.platform.iam.infrastructure.persistence.jpa.entities.PasswordResetTokenPersistenceEntity;

import java.util.Optional;

@Repository
public interface PasswordResetTokenPersistenceRepository extends JpaRepository<PasswordResetTokenPersistenceEntity, Long> {
    Optional<PasswordResetTokenPersistenceEntity> findByToken(String token);
}