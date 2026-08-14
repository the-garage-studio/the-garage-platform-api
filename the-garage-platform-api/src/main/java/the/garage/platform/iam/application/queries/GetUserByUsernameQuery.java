package the.garage.platform.iam.application.queries;

public record GetUserByUsernameQuery(String username) {
    public GetUserByUsernameQuery {
        if (username == null || username.isBlank()) throw new IllegalArgumentException("username cannot be null or blank");
    }
}