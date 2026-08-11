package the.garage.platform.notifications.domain.model.aggregates;

import lombok.Getter;
import lombok.Setter;
import the.garage.platform.notifications.domain.model.valueobjects.EmailAddress;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationContent;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationStatus;
import the.garage.platform.notifications.domain.model.valueobjects.NotificationType;
import the.garage.platform.shared.domain.model.aggregates.AbstractDomainAggregateRoot;

import java.time.LocalDateTime;

@Getter
public class Notification extends AbstractDomainAggregateRoot<Notification> {

    @Setter
    private Long id;

    private Long recipientId;
    private EmailAddress recipientEmail;
    private NotificationContent content;
    private NotificationType type;
    private NotificationStatus status;
    private LocalDateTime sentAt;

    /**
     * Kept for structural parity with the rest of the codebase; reconstruction
     * from persistence goes through {@link #reconstitute}, not this constructor.
     */
    protected Notification() {
    }

    private Notification(Long recipientId, EmailAddress recipientEmail, NotificationContent content,
                         NotificationType type, NotificationStatus status, LocalDateTime sentAt) {
        if (recipientId == null || recipientId <= 0) {
            throw new IllegalArgumentException("recipientId cannot be null or less than 1");
        }
        if (recipientEmail == null) throw new IllegalArgumentException("recipientEmail cannot be null");
        if (content == null) throw new IllegalArgumentException("content cannot be null");
        if (type == null) throw new IllegalArgumentException("type cannot be null");
        this.recipientId = recipientId;
        this.recipientEmail = recipientEmail;
        this.content = content;
        this.type = type;
        this.status = status;
        this.sentAt = sentAt;
    }

    /**
     * Factory method used by the application layer to create a new
     * notification, always starting as {@link NotificationStatus#PENDING}
     * before any delivery attempt is made.
     */
    public static Notification create(Long recipientId, EmailAddress recipientEmail,
                                      NotificationContent content, NotificationType type) {
        return new Notification(recipientId, recipientEmail, content, type, NotificationStatus.PENDING, null);
    }

    /**
     * Reconstructs a Notification from persisted state. Used exclusively by
     * the infrastructure assembler layer.
     */
    public static Notification reconstitute(Long id, Long recipientId, EmailAddress recipientEmail,
                                            NotificationContent content, NotificationType type,
                                            NotificationStatus status, LocalDateTime sentAt) {
        var notification = new Notification(recipientId, recipientEmail, content, type, status, sentAt);
        notification.id = id;
        return notification;
    }

    /** Marks the notification as successfully delivered. */
    public void markAsSent() {
        if (status == NotificationStatus.SENT) {
            throw new IllegalStateException("Notification was already sent");
        }
        this.status = NotificationStatus.SENT;
        this.sentAt = LocalDateTime.now();
    }

    /** Marks the notification as failed to deliver; can be retried later by the application layer. */
    public void markAsFailed() {
        if (status == NotificationStatus.SENT) {
            throw new IllegalStateException("A sent notification cannot be marked as failed");
        }
        this.status = NotificationStatus.FAILED;
    }

    public boolean isPending() {
        return status == NotificationStatus.PENDING;
    }

    public boolean isSent() {
        return status == NotificationStatus.SENT;
    }
}