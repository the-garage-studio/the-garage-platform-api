package the.garage.platform.iam.application.internal.eventhandlers;

import the.garage.platform.iam.application.internal.outboundservices.hashing.HashingService;
import the.garage.platform.iam.domain.model.aggregates.User;
import the.garage.platform.iam.domain.model.valueobjects.Email;
import the.garage.platform.iam.domain.model.valueobjects.Password;
import the.garage.platform.iam.domain.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Seeds the first Administrator account on application startup, if it doesn't
 * already exist. RN-06: an Administrator is never created through public
 * registration — only via this deployment-time seed.
 */
@Component
@Slf4j
public class AdministratorSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HashingService hashingService;

    @Value("${garage.admin.username}")
    private String adminUsername;

    @Value("${garage.admin.email}")
    private String adminEmail;

    @Value("${garage.admin.password}")
    private String adminPassword;

    public AdministratorSeeder(UserRepository userRepository, HashingService hashingService) {
        this.userRepository = userRepository;
        this.hashingService = hashingService;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(adminUsername)) {
            log.info("Administrator account '{}' already exists, skipping seed.", adminUsername);
            return;
        }
        var admin = User.seedAdministrator(
                "Garage",
                "Admin",
                adminUsername,
                new Email(adminEmail),
                Password.ofHash(hashingService.encode(adminPassword)));
        userRepository.save(admin);
        log.info("Seeded Administrator account with username '{}'.", adminUsername);
    }
}
