package the.garage.platform.notifications.infrastructure.persistence.jpa.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import the.garage.platform.notifications.domain.model.valueobjects.EmailAddress;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationStatus;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationType;
import the.garage.platform.notifications.infrastructure.persistence.jpa.converters.EmailAddressPersistenceConverter;
import the.garage.platform.shared.infrastructure.persistence.jpa.entities.AuditableAbstractPersistenceEntity;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
public class NotificationPersistenceEntity extends AuditableAbstractPersistenceEntity {

    @Column(name = "recipient_id", nullable = false)
    private Long recipientId;

    @Convert(converter = EmailAddressPersistenceConverter.class)
    @Column(name = "recipient_email", nullable = false, length = 150)
    private EmailAddress recipientEmail;

    @Column(nullable = false, length = 150)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String body;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private NotificationType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private NotificationStatus status;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}