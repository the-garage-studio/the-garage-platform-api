package the.garage.platform.iam.domain.repositories;

import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.iam.domain.model.valueobjects.Email;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(Email email);
    List<User> findAll();
    User save(User user);
    boolean existsByUsername(String username);
    boolean existsByEmail(Email email);
}