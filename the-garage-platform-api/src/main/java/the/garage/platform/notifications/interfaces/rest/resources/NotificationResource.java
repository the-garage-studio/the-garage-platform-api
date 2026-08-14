package the.garage.platform.notifications.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "NotificationResponse",
        description = "Notification history entry",
        example = "{\"id\": 1, \"type\": \"EMAIL_VERIFICATION\", \"subject\": \"Verifica tu cuenta en The Garage\", \"status\": \"SENT\", \"sentAt\": \"2026-08-11T14:30:00\"}"
)
public record NotificationResource(
        @Schema(description = "Notification unique identifier", example = "1")
        Long id,

        @Schema(description = "Notification type", example = "EMAIL_VERIFICATION")
        String type,

        @Schema(description = "Email subject", example = "Verifica tu cuenta en The Garage")
        String subject,

        @Schema(description = "Delivery status", example = "SENT", allowableValues = {"PENDING", "SENT", "FAILED"})
        String status,

        @Schema(description = "Timestamp when the notification was successfully sent, null if not yet sent", example = "2026-08-11T14:30:00")
        String sentAt
) {
}