package org.factory.dst.api;

import org.factory.dst.persistence.entity.Datapoint;
import org.factory.dst.persistence.repository.DatapointRepository;
import org.factory.dst.process.dto.DatapointDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ManagementTest extends AbstractIntegrationTest {

    @Autowired
    private DatapointRepository datapointRepository;

    @BeforeEach
    void setUp() {
        datapointRepository.deleteAll();
    }

    @Test
    void storeUniqueDatapoint() {
        final String url = getBaseUrl() + "/datapoints";
        final OffsetDateTime now = OffsetDateTime.now();
        final DatapointDto request = new DatapointDto(now, 12.34, "HMS", "me");
        final DatapointDto expectedDatapoint = new DatapointDto(request.getTimestamp().toInstant().atOffset(ZoneOffset.UTC),
                request.getValue(), request.getDevice(), request.getUser());

        DatapointDto response = given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(201)
                .extract()
                .as(DatapointDto.class);
        assertEquals(expectedDatapoint, response);
        checkStoredDatapoint(expectedDatapoint);

        // second request with the same timestamp, device and user should fail
        request.setValue(100);
        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post(url)
                .then()
                .statusCode(400);
        checkStoredDatapoint(expectedDatapoint);
    }

    private void checkStoredDatapoint(final DatapointDto expected) {
        Datapoint stored = datapointRepository.findFirstByDevice(expected.getDevice()).orElse(null);
        assertNotNull(stored);
        assertEquals(expected.getTimestamp().atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime(), stored.getTimestamp());
        assertEquals(expected.getValue(), stored.getValue());
        assertEquals(expected.getDevice(), stored.getDevice());
        assertEquals(expected.getUser(), stored.getUser());
    }
}
