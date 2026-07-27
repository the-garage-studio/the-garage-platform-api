package the.garage.platform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class TheGaragePlatformApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TheGaragePlatformApiApplication.class, args);
    }

}
