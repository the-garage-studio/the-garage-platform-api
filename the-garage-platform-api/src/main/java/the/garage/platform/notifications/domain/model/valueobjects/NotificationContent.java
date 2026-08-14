package the.garage.platform.notifications.domain.model.valueobjects;

/**
 * NotificationContent value object.
 *
 * <p>Groups the subject and body together so a {@link com.thegarage.platform.notifications.domain.model.aggregates.Notification}
 * can never exist with one but not the other.</p>
 */
public record NotificationContent(String subject, String body) {

    public NotificationContent {
        if (subject == null || subject.isBlank()) {
            throw new IllegalArgumentException("subject cannot be null or blank");
        }
        if (body == null || body.isBlank()) {
            throw new IllegalArgumentException("body cannot be null or blank");
        }
    }
}