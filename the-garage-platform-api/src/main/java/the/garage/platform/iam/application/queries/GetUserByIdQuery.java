package the.garage.platform.iam.application.queries;

public record GetUserByIdQuery(Long userId) {
    public GetUserByIdQuery {
        if (userId == null || userId <= 0) throw new IllegalArgumentException("userId cannot be null or less than 1");
    }
}