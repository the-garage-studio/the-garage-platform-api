package the.garage.platform.iam.infrastructure.persistence.jpa.adapters;

import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.domain.repositories.UserRepository;
import the.garage.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import the.garage.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserPersistenceRepository userPersistenceRepository;

    public UserRepositoryImpl(UserPersistenceRepository userPersistenceRepository) {
        this.userPersistenceRepository = userPersistenceRepository;
    }

    @Override
    public Optional<User> findById(Long id) {
        return userPersistenceRepository.findById(id).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userPersistenceRepository.findByUsername(username).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findByEmail(Email email) {
        return userPersistenceRepository.findByEmail(email).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<User> findAll() {
        return userPersistenceRepository.findAll().stream().map(UserPersistenceAssembler::toDomainFromPersistence).toList();
    }

    @Override
    public User save(User user) {
        var saved = userPersistenceRepository.save(UserPersistenceAssembler.toPersistenceFromDomain(user));
        return UserPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userPersistenceRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return userPersistenceRepository.countByEmail(email) > 0;
    }
}
