package the.garage.platform.notifications.application.queries;

public record GetNotificationHistoryQuery(Long recipientId) {
    public GetNotificationHistoryQuery {
        if (recipientId == null || recipientId <= 0) {
            throw new IllegalArgumentException("recipientId cannot be null or less than 1");
        }
    }
}