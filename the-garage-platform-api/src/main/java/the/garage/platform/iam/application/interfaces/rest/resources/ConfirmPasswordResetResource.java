package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(
        name = "ConfirmPasswordResetRequest",
        description = "Request payload to finish the password reset flow",
        example = "{\"token\": \"a1b2c3d4-...\", \"newPassword\": \"NewSecurePass456!\"}"
)
public record ConfirmPasswordResetResource(
        @Schema(description = "Password reset token received by email", example = "a1b2c3d4-e5f6-...")
        String token,

        @Schema(description = "New password", example = "NewSecurePass456!", minLength = 8, maxLength = 255)
        String newPassword
) {
}
