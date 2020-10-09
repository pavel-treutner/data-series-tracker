package org.factory.dst.process.service;

import org.factory.dst.persistence.entity.Datapoint;
import org.factory.dst.persistence.repository.DatapointRepository;
import org.factory.dst.process.dto.AverageDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

public class StatisticsServiceTest {

    private final DatapointRepository datapointRepository = Mockito.mock(DatapointRepository.class);
    private final StatisticsService subject = new StatisticsService(datapointRepository);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(datapointRepository);
    }

    private static Stream<Arguments> getAveragesByDeviceSuccessSource() {
        return Stream.of(
                Arguments.of(
                        List.of(
                                new Datapoint(null, LocalDateTime.parse("2020-07-20T10:15:30"), 1, null, null),
                                new Datapoint(null, LocalDateTime.parse("2020-07-20T10:19:55"), 4, null, null),
                                new Datapoint(null, LocalDateTime.parse("2020-07-20T10:17:10"), 4, null, null),
                                new Datapoint(null, LocalDateTime.parse("2020-07-20T10:30:00"), 8, null, null),
                                new Datapoint(null, LocalDateTime.parse("2020-07-21T15:07:05"), 15, null, null),
                                new Datapoint(null, LocalDateTime.parse("1970-01-01T00:03:00"), 30, null, null),
                                new Datapoint(null, LocalDateTime.parse("1969-12-31T23:58:43"), 40, null, null),
                                new Datapoint(null, LocalDateTime.parse("1930-02-02T08:04:02"), 20, null, null)
                        ),
                        List.of(
                                new AverageDto(OffsetDateTime.parse("1930-02-02T08:00:00Z"), 20),
                                new AverageDto(OffsetDateTime.parse("1969-12-31T23:45:00Z"), 40),
                                new AverageDto(OffsetDateTime.parse("1970-01-01T00:00:00Z"), 30),
                                new AverageDto(OffsetDateTime.parse("2020-07-20T10:15:00Z"), 3),
                                new AverageDto(OffsetDateTime.parse("2020-07-20T10:30:00Z"), 8),
                                new AverageDto(OffsetDateTime.parse("2020-07-21T15:00:00Z"), 15)
                        )
                )
        );
    }

    @MethodSource("getAveragesByDeviceSuccessSource")
    @ParameterizedTest
    void getAveragesByDeviceSuccess(List<Datapoint> datapoints, List<AverageDto> expected) {
        final String device = "LemarchandsBox";
        Mockito.when(datapointRepository.findByDevice(device)).thenReturn(datapoints);
        List<AverageDto> result = subject.getAveragesByDevice(device);
        Assertions.assertEquals(expected, result);
        Mockito.verify(datapointRepository, Mockito.times(1)).findByDevice(device);
    }
}
