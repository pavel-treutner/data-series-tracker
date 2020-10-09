package org.factory.dst.process.service;

import org.factory.dst.persistence.entity.Datapoint;
import org.factory.dst.persistence.repository.DatapointRepository;
import org.factory.dst.process.dto.AverageDto;
import org.factory.dst.process.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service producing statistics data.
 */
@Service
public class StatisticsService {

    /**
     * Duration in seconds.
     */
    private static final long TIME_WINDOW_DURATION = 15 * 60;

    private final DatapointRepository datapointRepository;

    @Autowired
    public StatisticsService(DatapointRepository datapointRepository) {
        this.datapointRepository = datapointRepository;
    }

    /**
     * Get 15 minutes average values for a device.
     *
     * @param device Device id.
     * @throws NotFoundException If no datapoint is found.
     */
    @Transactional(readOnly = true)
    public List<AverageDto> getAveragesByDevice(final String device) {
        return getAverages(datapointRepository.findByDevice(device), "device");
    }

    /**
     * Get 15 minutes average values for a user.
     *
     * @param user User id.
     * @throws NotFoundException If no datapoint is found.
     */
    @Transactional(readOnly = true)
    public List<AverageDto> getAveragesByUser(final String user) {
        return getAverages(datapointRepository.findByUser(user), "user");
    }

    private List<AverageDto> getAverages(final List<Datapoint> datapoints, final String entityName) {
        if (datapoints.isEmpty()) {
            throw new NotFoundException("No datapoint for " + entityName + " found.");
        }

        // Key is the number of time windows since Unix epoch
        Map<Long, List<Datapoint>> windowValues = datapoints.stream().collect(Collectors.groupingBy(this::assignTimeWindow));

        return windowValues.entrySet().stream().map(
                e -> new AverageDto(
                        Instant.ofEpochSecond(e.getKey() * TIME_WINDOW_DURATION).atOffset(ZoneOffset.UTC),
                        e.getValue().stream().collect(Collectors.averagingDouble(Datapoint::getValue))
                )
        ).sorted(Comparator.comparing(AverageDto::getTimestamp)).collect(Collectors.toList());
    }

    private long assignTimeWindow(Datapoint datapoint) {
        long timestamp = datapoint.getTimestamp().atZone(ZoneOffset.UTC).toEpochSecond();
        long window = timestamp / TIME_WINDOW_DURATION;
        return timestamp < 0 ? --window : window;
    }
}
