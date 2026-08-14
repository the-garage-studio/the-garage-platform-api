package the.garage.platform.notifications.domain.exceptions;

public class NotificationAlreadySentException extends RuntimeException {
    public NotificationAlreadySentException(Long notificationId) {
        super("Notification %s was already sent".formatted(notificationId));
    }
}