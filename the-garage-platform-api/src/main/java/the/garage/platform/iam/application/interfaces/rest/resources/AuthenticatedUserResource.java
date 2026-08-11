package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "AuthenticatedUserResponse",
        description = "Authenticated user information with JWT token",
        example = "{\"id\": 1, \"username\": \"john.doe\", \"role\": \"COLLECTOR\", \"token\": \"eyJhbGciOiJIUzI1NiIs...\"}"
)
public record AuthenticatedUserResource(
        @Schema(description = "User unique identifier", example = "1")
        Long id,

        @Schema(description = "Username", example = "john.doe")
        String username,

        @Schema(description = "Assigned role", example = "COLLECTOR")
        String role,

        @Schema(description = "JWT Bearer token for authentication", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String token
) {
}