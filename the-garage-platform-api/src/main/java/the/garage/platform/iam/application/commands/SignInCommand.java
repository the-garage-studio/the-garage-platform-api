package the.garage.platform.iam.application.commands;

/**
 * Command to authenticate an existing user.
 */
public record SignInCommand(String username, String password) {
}