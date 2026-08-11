package the.garage.platform.iam.application.interfaces.rest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the.garage.platform.iam.application.commands.ConfirmPasswordResetCommand;
import the.garage.platform.iam.application.commands.RequestPasswordResetCommand;
import the.garage.platform.iam.application.commands.VerifyEmailCommand;
import the.garage.platform.iam.application.commandservices.EmailVerificationCommandService;
import the.garage.platform.iam.application.commandservices.PasswordResetCommandService;
import the.garage.platform.iam.application.commandservices.UserCommandService;
import the.garage.platform.iam.application.interfaces.rest.resources.*;
import the.garage.platform.iam.application.interfaces.rest.transform.AuthenticatedUserResourceFromEntityAssembler;
import the.garage.platform.iam.application.interfaces.rest.transform.RegisterCollectorCommandFromResourceAssembler;
import the.garage.platform.iam.application.interfaces.rest.transform.SignInCommandFromResourceAssembler;
import the.garage.platform.iam.application.interfaces.rest.transform.UserResourceFromEntityAssembler;
import the.garage.platform.shared.interfaces.rest.resources.MessageResource;
import the.garage.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;

@RestController
@RequestMapping(value = "/api/v1/authentication", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Authentication", description = "Authentication, registration, and account recovery endpoints")
public class AuthenticationController {

    private final UserCommandService userCommandService;
    private final PasswordResetCommandService passwordResetCommandService;
    private final EmailVerificationCommandService emailVerificationCommandService;

    public AuthenticationController(
            UserCommandService userCommandService,
            PasswordResetCommandService passwordResetCommandService,
            EmailVerificationCommandService emailVerificationCommandService) {
        this.userCommandService = userCommandService;
        this.passwordResetCommandService = passwordResetCommandService;
        this.emailVerificationCommandService = emailVerificationCommandService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Register a new collector",
            description = "Creates a new Collector account. The system always assigns the COLLECTOR role and starts the account as PENDING_VERIFICATION."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Collector registered successfully",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data"),
            @ApiResponse(responseCode = "409", description = "Conflict - email or username already exists")
    })
    public ResponseEntity<?> registerCollector(@RequestBody RegisterCollectorResource resource) {
        var command = RegisterCollectorCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result, UserResourceFromEntityAssembler::toResourceFromEntity, HttpStatus.CREATED);
    }

    @PostMapping("/sign-in")
    @Operation(
            summary = "User sign-in",
            description = "Authenticates a user and returns a JWT token. Accounts that are not ACTIVE are rejected (RN-07)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User authenticated successfully",
                    content = @Content(schema = @Schema(implementation = AuthenticatedUserResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid credentials"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "422", description = "Account is not active")
    })
    public ResponseEntity<?> signIn(@RequestBody SignInResource resource) {
        var command = SignInCommandFromResourceAssembler.toCommandFromResource(resource);
        var result = userCommandService.handle(command);
        return ResponseEntityAssembler.toResponseEntityFromResult(
                result,
                auth -> AuthenticatedUserResourceFromEntityAssembler.toResourceFromEntity(auth.getLeft(), auth.getRight()),
                HttpStatus.OK
        );
    }

    @PostMapping("/verify-email")
    @Operation(
            summary = "Verify account email",
            description = "Activates a PENDING_VERIFICATION account using the token sent by email."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email verified successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Token not found"),
            @ApiResponse(responseCode = "422", description = "Token already used or expired")
    })
    public ResponseEntity<?> verifyEmail(@RequestBody VerifyEmailResource resource) {
        var command = new VerifyEmailCommand(resource.token());
        var result = emailVerificationCommandService.handle(command)
                .map(ignored -> new MessageResource("Email verified successfully"));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }

    @PostMapping("/password-reset/request")
    @Operation(
            summary = "Request password reset",
            description = "Sends a password reset link if an account with the given email exists. Always returns a generic confirmation message."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request processed",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "400", description = "Invalid email format")
    })
    public ResponseEntity<?> requestPasswordReset(@RequestBody RequestPasswordResetResource resource) {
        var command = new RequestPasswordResetCommand(resource.email());
        var result = passwordResetCommandService.handle(command)
                .map(MessageResource::new);
        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }

    @PostMapping("/password-reset/confirm")
    @Operation(
            summary = "Confirm password reset",
            description = "Sets a new password using a previously issued, non-expired reset token."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Password updated successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "404", description = "Token not found"),
            @ApiResponse(responseCode = "422", description = "Token already used or expired")
    })
    public ResponseEntity<?> confirmPasswordReset(@RequestBody ConfirmPasswordResetResource resource) {
        var command = new ConfirmPasswordResetCommand(resource.token(), resource.newPassword());
        var result = passwordResetCommandService.handle(command)
                .map(ignored -> new MessageResource("Password updated successfully"));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }
}