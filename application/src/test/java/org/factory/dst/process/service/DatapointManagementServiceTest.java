package org.factory.dst.process.service;

import org.factory.dst.persistence.entity.Datapoint;
import org.factory.dst.persistence.repository.DatapointRepository;
import org.factory.dst.process.exception.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.Optional;

class DatapointManagementServiceTest {

    private final DatapointRepository datapointRepository = Mockito.mock(DatapointRepository.class);
    private final DatapointManagementService subject = new DatapointManagementService(datapointRepository);

    @BeforeEach
    void beforeEach() {
        Mockito.reset(datapointRepository);
    }

    @Test
    void deleteByDeviceSuccess() {
        final String device = "LemarchandsBox";
        Mockito.when(datapointRepository.findFirstByDevice(device)).thenReturn(Optional.of(new Datapoint()));
        subject.deleteByDevice(device);
        Mockito.verify(datapointRepository, Mockito.times(1)).deleteByDevice(device);
    }

    @Test
    void deleteByDeviceNotFound() {
        final String device = "LemarchandsBox";
        Mockito.when(datapointRepository.findFirstByDevice(device)).thenReturn(Optional.empty());
        NotFoundException exception = Assertions.assertThrows(NotFoundException.class, () -> subject.deleteByDevice(device));
        Assertions.assertEquals("No datapoint for device found.", exception.getMessage());
        Mockito.verify(datapointRepository, Mockito.never()).deleteByDevice(ArgumentMatchers.any());
    }
}
