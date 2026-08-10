package the.garage.platform.iam.application.interfaces.rest.transform;

import the.garage.platform.iam.application.interfaces.rest.resources.UserResource;
import the.garage.platform.iam.domain.model.aggregates.User;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        return new UserResource(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getEmail().address(),
                user.getRole().name(),
                user.getStatus().name());
    }
}