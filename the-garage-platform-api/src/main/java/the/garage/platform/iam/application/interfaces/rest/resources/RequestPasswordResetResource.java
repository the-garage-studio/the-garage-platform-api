package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "RequestPasswordResetRequest",
        description = "Request payload to start the password reset flow",
        example = "{\"email\": \"john.doe@example.com\"}"
)
public record RequestPasswordResetResource(
        @Schema(description = "Registered email address", example = "john.doe@example.com")
        String email
) {
}