package org.factory.dst.process.service;

import org.factory.dst.persistence.repository.DatapointRepository;
import org.factory.dst.process.converter.DatapointDtoConverter;
import org.factory.dst.process.dto.DatapointDto;
import org.factory.dst.process.exception.NotFoundException;
import org.factory.dst.process.exception.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service responsible for datapoint management.
 */
@Service
public class DatapointManagementService {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final DatapointRepository datapointRepository;

    @Autowired
    public DatapointManagementService(DatapointRepository datapointRepository) {
        this.datapointRepository = datapointRepository;
    }

    /**
     * Store a new datapoint.
     *
     * @param datapoint Instance to store.
     * @return Stored instance.
     * @throws ValidationException On validation errors.
     */
    public DatapointDto store(final DatapointDto datapoint) {
        log.info("Storing new datapoint: {}", datapoint);
        try {
            return DatapointDtoConverter.toDto(datapointRepository.save(DatapointDtoConverter.fromDto(datapoint)));
        } catch (final DataIntegrityViolationException exception) {
            throw new ValidationException("Datapoint for passed combination of timestamp, device and user already exists.");
        }
    }

    /**
     * Delete all datapoints assigned to a device.
     *
     * @param device Device id.
     * @throws NotFoundException If no datapoint is found.
     */
    @Transactional
    public void deleteByDevice(final String device) {
        datapointRepository.findFirstByDevice(device).orElseThrow(() -> new NotFoundException("No datapoint for device found."));
        log.info("Deleting datapoints by device {}", device);
        datapointRepository.deleteByDevice(device);
    }

    /**
     * Delete all datapoints assigned to a user.
     *
     * @param user User id.
     * @throws NotFoundException If no datapoint is found.
     */
    @Transactional
    public void deleteByUser(final String user) {
        datapointRepository.findFirstByUser(user).orElseThrow(() -> new NotFoundException("No datapoint for user found."));
        log.info("Deleting datapoints by user {}", user);
        datapointRepository.deleteByUser(user);
    }
}
