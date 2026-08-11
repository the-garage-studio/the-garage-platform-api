package the.garage.platform.iam.application.interfaces.rest.transform;

import the.garage.platform.iam.application.interfaces.rest.resources.AuthenticatedUserResource;
import the.garage.platform.iam.domain.model.aggregates.User;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(
                user.getId(),
                user.getUsername(),
                user.getRole().name(),token
        );
    }
}
