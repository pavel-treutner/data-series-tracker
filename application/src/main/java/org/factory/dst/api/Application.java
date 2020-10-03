package org.factory.dst.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main application.
 */
@SpringBootApplication(scanBasePackages = { Application.BASE_PACKAGE })
@EnableJpaRepositories(basePackages = { Application.PERSISTENCE_PACKAGE })
@EntityScan(basePackages = { Application.PERSISTENCE_PACKAGE })
public class Application {

    static final String BASE_PACKAGE = "org.factory.dst";
    static final String PERSISTENCE_PACKAGE = BASE_PACKAGE + ".persistence";

    /**
     * An entry point.
     *
     * @param args Arguments.
     */
    public static void main(final String... args) {
        SpringApplication.run(Application.class, args);
    }
}
