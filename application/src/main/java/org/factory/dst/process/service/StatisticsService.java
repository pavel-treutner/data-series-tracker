package org.factory.dst.process.service;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.factory.dst.persistence.entity.Datapoint;
import org.factory.dst.persistence.repository.DatapointRepository;
import org.factory.dst.process.dto.AverageDto;
import org.factory.dst.process.exception.NotFoundException;
import org.factory.dst.process.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<AverageDto> getAveragesByDevice(final String device) {
        List<Datapoint> data = datapointRepository.findByDevice(device);
        return computeAverages(data, "device");
    }

    /**
     * Get 15 minutes moving average values for a device.
     *
     * @param device Device id.
     * @param windowSize Window size.
     * @throws ValidationException On validation errors.
     * @throws NotFoundException If no datapoint is found.
     */
    public List<AverageDto> getMovingAveragesByDevice(final String device, final int windowSize) {
        checkWindowSize(windowSize);
        List<AverageDto> averages = getAveragesByDevice(device);
        return computeMovingAverages(averages, windowSize);
    }

    /**
     * Get 15 minutes average values for a user.
     *
     * @param user User id.
     * @throws NotFoundException If no datapoint is found.
     */
    public List<AverageDto> getAveragesByUser(final String user) {
        List<Datapoint> data = datapointRepository.findByUser(user);
        return computeAverages(data, "user");
    }

    /**
     * Get 15 minutes moving average values for a user.
     *
     * @param user User id.
     * @param windowSize Window size.
     * @throws ValidationException On validation errors.
     * @throws NotFoundException If no datapoint is found.
     */
    public List<AverageDto> getMovingAveragesByUser(final String user, final int windowSize) {
        checkWindowSize(windowSize);
        List<AverageDto> averages = getAveragesByUser(user);
        return computeMovingAverages(averages, windowSize);
    }

    private List<AverageDto> computeAverages(final List<Datapoint> datapoints, final String entityName) {
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

    private long assignTimeWindow(final Datapoint datapoint) {
        long timestamp = datapoint.getTimestamp().atZone(ZoneOffset.UTC).toEpochSecond();
        long window = timestamp / TIME_WINDOW_DURATION;
        return timestamp < 0 ? --window : window;
    }

    private void checkWindowSize(final int windowSize) {
        if (windowSize < 1) {
            throw new ValidationException("Window size must be greater than 0.");
        }
    }

    private List<AverageDto> computeMovingAverages(final List<AverageDto> averages, final int windowSize) {
        final DescriptiveStatistics statistics = new DescriptiveStatistics(windowSize);
        return averages.stream().map(a -> {
            statistics.addValue(a.getValue());
            return new AverageDto(a.getTimestamp(), statistics.getMean());
        }).collect(Collectors.toList());
    }
}
