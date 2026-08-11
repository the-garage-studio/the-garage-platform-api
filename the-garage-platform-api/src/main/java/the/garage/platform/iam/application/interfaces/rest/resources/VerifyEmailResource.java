package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "VerifyEmailRequest",
        description = "Request payload to verify a newly registered account's email",
        example = "{\"token\": \"a1b2c3d4-...\"}"
)
public record VerifyEmailResource(
        @Schema(description = "Email verification token received by email", example = "a1b2c3d4-e5f6-...")
        String token
) {
}