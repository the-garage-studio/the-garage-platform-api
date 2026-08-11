package the.garage.platform.iam.application.interfaces.rest;

import the.garage.platform.iam.application.commandservices.UserCommandService;
import the.garage.platform.iam.application.interfaces.rest.resources.ChangeUserRoleResource;
import the.garage.platform.iam.application.interfaces.rest.resources.UserResource;
import the.garage.platform.iam.application.interfaces.rest.transform.ChangeUserRoleCommandFromResourceAssembler;
import the.garage.platform.iam.application.interfaces.rest.transform.UserResourceFromEntityAssembler;
import the.garage.platform.iam.application.queries.GetAllUsersQuery;
import the.garage.platform.iam.application.queries.GetUserByIdQuery;
import the.garage.platform.iam.application.queryservices.UserQueryService;

import the.garage.platform.shared.interfaces.rest.resources.MessageResource;
import the.garage.platform.shared.interfaces.rest.transform.ResponseEntityAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "User profile and account management endpoints")
public class UsersController {

    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UsersController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @GetMapping
    @Operation(
            summary = "Get all users",
            description = "Retrieves all user accounts. Requires Administrator role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Users retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions")
    })
    public ResponseEntity<List<UserResource>> getAllUsers() {
        var users = userQueryService.handle(new GetAllUsersQuery());
        var userResources = users.stream().map(UserResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(userResources);
    }

    @GetMapping("/{userId}")
    @Operation(
            summary = "Get user by ID",
            description = "Retrieves a specific user's information by unique identifier.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully",
                    content = @Content(schema = @Schema(implementation = UserResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> getUserById(
            @PathVariable
            @Parameter(description = "Unique user identifier", example = "1", required = true)
            Long userId
    ) {
        var user = userQueryService.handle(new GetUserByIdQuery(userId));
        if (user.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(UserResourceFromEntityAssembler.toResourceFromEntity(user.get()));
    }

    @PostMapping("/{userId}/role")
    @Operation(
            summary = "Change user role",
            description = "Changes a user's role. Requires Administrator role (RN-43).",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Role changed successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden - Insufficient permissions"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<?> changeUserRole(
            @PathVariable Long userId,
            @RequestBody ChangeUserRoleResource resource
    ) {
        var command = ChangeUserRoleCommandFromResourceAssembler.toCommandFromResource(userId, resource);
        var result = userCommandService.handle(command)
                .map(ignored -> new MessageResource("Role changed successfully"));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }

    @PostMapping("/{userId}/suspension")
    @Operation(
            summary = "Suspend user account",
            description = "Suspends a user account (RN-07). Requires Administrator role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User suspended successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - user already suspended")
    })
    public ResponseEntity<?> suspendUser(@PathVariable Long userId) {
        var result = userCommandService.handle(new the.garage.platform.iam.application.commands.SuspendUserCommand(userId))
                .map(ignored -> new MessageResource("User suspended successfully"));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }

    @PostMapping("/{userId}/reactivation")
    @Operation(
            summary = "Reactivate user account",
            description = "Reactivates a previously suspended user account. Requires Administrator role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User reactivated successfully",
                    content = @Content(schema = @Schema(implementation = MessageResource.class))),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "409", description = "Conflict - user is not suspended")
    })
    public ResponseEntity<?> reactivateUser(@PathVariable Long userId) {
        var result = userCommandService.handle(new the.garage.platform.iam.application.commands.ReactivateUserCommand(userId))
                .map(ignored -> new MessageResource("User reactivated successfully"));
        return ResponseEntityAssembler.toResponseEntityFromResult(result, message -> message, HttpStatus.OK);
    }
}
