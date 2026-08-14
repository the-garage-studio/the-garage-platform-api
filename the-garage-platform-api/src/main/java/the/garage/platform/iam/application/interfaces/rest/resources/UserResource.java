package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "UserResponse",
        description = "User information response",
        example = "{\"id\": 1, \"firstName\": \"John\", \"lastName\": \"Doe\", \"username\": \"john.doe\", \"email\": \"john.doe@example.com\", \"role\": \"COLLECTOR\", \"status\": \"ACTIVE\"}"
)
public record UserResource(
        @Schema(description = "User unique identifier", example = "1")
        Long id,

        @Schema(description = "First name", example = "John")
        String firstName,

        @Schema(description = "Last name", example = "Doe")
        String lastName,

        @Schema(description = "Username", example = "john.doe")
        String username,

        @Schema(description = "Email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Assigned role", example = "COLLECTOR", allowableValues = {"COLLECTOR", "ADMIN"})
        String role,

        @Schema(description = "Account status", example = "ACTIVE", allowableValues = {"PENDING_VERIFICATION", "ACTIVE", "SUSPENDED", "DELETED"})
        String status
) {
}
