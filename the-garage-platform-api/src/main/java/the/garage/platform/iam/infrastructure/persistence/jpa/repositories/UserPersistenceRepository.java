package the.garage.platform.iam.infrastructure.persistence.jpa.repositories;

import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.infrastructure.persistence.jpa.entities.UserPersistenceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


@Repository
public interface UserPersistenceRepository extends JpaRepository<UserPersistenceEntity, Long> {

    Optional<UserPersistenceEntity> findByUsername(String username);

    boolean existsByUsername(String username);

    @Query("select u from UserPersistenceEntity u where u.email = :email")
    Optional<UserPersistenceEntity> findByEmail(@Param("email") Email email);

    @Query("select count(u) from UserPersistenceEntity u where u.email = :email")
    long countByEmail(@Param("email") Email email);
}
