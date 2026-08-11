package the.garage.platform.iam.application.commands;

public record RegisterCollectorCommand(
        String firstName, String lastName, String username, String email, String rawPassword) {

    public RegisterCollectorCommand {
        if (firstName == null || firstName.isBlank()) throw new IllegalArgumentException("firstName cannot be null or blank");
        if (lastName == null || lastName.isBlank()) throw new IllegalArgumentException("lastName cannot be null or blank");
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username cannot be null or blank");
        if (email == null || email.isBlank()) throw new IllegalArgumentException("email cannot be null or blank");
        if (rawPassword == null || rawPassword.isBlank()) throw new IllegalArgumentException("rawPassword cannot be null or blank");
    }
}