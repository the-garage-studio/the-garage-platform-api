package the.garage.platform.notifications.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the.garage.platform.notifications.application.queries.GetNotificationHistoryQuery;
import the.garage.platform.notifications.application.queryservices.NotificationQueryService;
import the.garage.platform.notifications.interfaces.rest.resources.NotificationResource;
import the.garage.platform.notifications.interfaces.rest.transform.NotificationResourceFromEntityAssembler;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/notifications", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Notifications", description = "Notification history endpoints")
public class NotificationsController {

    private final NotificationQueryService notificationQueryService;

    public NotificationsController(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    @GetMapping("/recipients/{recipientId}")
    @Operation(
            summary = "Get notification history for a recipient",
            description = "Retrieves all notifications sent to a specific recipient, ordered by creation. Requires authentication.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Notification history retrieved successfully",
                    content = @Content(schema = @Schema(implementation = NotificationResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<List<NotificationResource>> getNotificationHistory(
            @PathVariable
            @Parameter(description = "Recipient's unique identifier", example = "1", required = true)
            Long recipientId
    ) {
        var query = new GetNotificationHistoryQuery(recipientId);
        var notifications = notificationQueryService.handle(query);
        var resources = notifications.stream().map(NotificationResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(resources);
    }
}