package the.garage.platform.notifications.infrastructure.persistence.jpa.adapters;

import org.springframework.stereotype.Repository;
import the.garage.platform.notifications.domain.model.aggregates.Notification;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationStatus;
import the.garage.platform.notifications.domain.repositories.NotificationRepository;
import the.garage.platform.notifications.infrastructure.persistence.jpa.assemblers.NotificationPersistenceAssembler;
import the.garage.platform.notifications.infrastructure.persistence.jpa.repositories.NotificationPersistenceRepository;

import java.util.List;
import java.util.Optional;

@Repository
public class NotificationRepositoryImpl implements NotificationRepository {

    private final NotificationPersistenceRepository persistenceRepository;

    public NotificationRepositoryImpl(NotificationPersistenceRepository persistenceRepository) {
        this.persistenceRepository = persistenceRepository;
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return persistenceRepository.findById(id).map(NotificationPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<Notification> findByRecipientId(Long recipientId) {
        return persistenceRepository.findByRecipientId(recipientId).stream()
                .map(NotificationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public List<Notification> findByStatus(NotificationStatus status) {
        return persistenceRepository.findByStatus(status).stream()
                .map(NotificationPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public Notification save(Notification notification) {
        var saved = persistenceRepository.save(NotificationPersistenceAssembler.toPersistenceFromDomain(notification));
        return NotificationPersistenceAssembler.toDomainFromPersistence(saved);
    }
}