package org.factory.dst.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application.
 */
@SpringBootApplication(
        scanBasePackages = {
                "org.factory.dst"
        }
)
public class Application {

    /**
     * An entry point.
     *
     * @param args Arguments.
     */
    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }
}
