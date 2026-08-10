package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ChangeUserRoleRequest",
        description = "Request payload to change a user's role (Administrator only)",
        example = "{\"role\": \"ADMIN\"}"
)
public record ChangeUserRoleResource(
        @Schema(description = "New role to assign", example = "ADMIN", allowableValues = {"COLLECTOR", "ADMIN"})
        String role
) {
}