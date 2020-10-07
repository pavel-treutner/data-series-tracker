package org.factory.dst.api;

import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * Integration tests parent.
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(
        classes = {Application.class},
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@ExtendWith(SpringExtension.class)
public abstract class AbstractIntegrationTest {

    @LocalServerPort
    protected int port;

    /**
     * Return base URL of the running application.
     *
     * @return Base URL.
     */
    protected String getBaseUrl() {
        return "http://localhost:" + port;
    }
}
