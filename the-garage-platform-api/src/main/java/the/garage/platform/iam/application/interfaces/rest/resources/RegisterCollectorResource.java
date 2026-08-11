package the.garage.platform.iam.application.interfaces.rest.resources;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resource received to register a new Collector account.
 * Public registration never accepts a role — the system always assigns COLLECTOR (RN-06).
 */
@Schema(
        name = "RegisterCollectorRequest",
        description = "Public collector registration request",
        example = "{\"firstName\": \"John\", \"lastName\": \"Doe\", \"username\": \"john.doe\", \"email\": \"john.doe@example.com\", \"password\": \"SecurePass123!\"}"
)
public record RegisterCollectorResource(
        @Schema(description = "First name", example = "John", minLength = 1, maxLength = 50)
        String firstName,

        @Schema(description = "Last name", example = "Doe", minLength = 1, maxLength = 50)
        String lastName,

        @Schema(description = "Desired username", example = "john.doe", minLength = 3, maxLength = 50)
        String username,

        @Schema(description = "Email address", example = "john.doe@example.com")
        String email,

        @Schema(description = "Password (minimum 8 characters)", example = "SecurePass123!", minLength = 8, maxLength = 255)
        String password
) {
}