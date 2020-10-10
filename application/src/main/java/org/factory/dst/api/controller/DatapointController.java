package org.factory.dst.api.controller;

import org.factory.dst.process.dto.DatapointDto;
import org.factory.dst.process.service.DatapointManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * Datapoint management methods.
 */
@RestController
public class DatapointController {

    private final DatapointManagementService datapointManagementService;

    @Autowired
    public DatapointController(DatapointManagementService datapointManagementService) {
        this.datapointManagementService = datapointManagementService;
    }

    /**
     * Store a new datapoint.
     *
     * @param datapoint Instance to store.
     * @return Stored instance.
     */
    @RequestMapping(value = "/datapoints",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.POST)
    public ResponseEntity<DatapointDto> storeDatapoint(@Valid @RequestBody final DatapointDto datapoint) {
        return ResponseEntity.status(HttpStatus.CREATED).body(datapointManagementService.store(datapoint));
    }

    /**
     * Delete datapoints by device.
     *
     * @param device Device id.
     * @return No content.
     */
    @RequestMapping(value = "/devices/{device}/datapoints", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDatapointsByDevice(@PathVariable("device") String device) {
        datapointManagementService.deleteByDevice(device);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Delete datapoints by user.
     *
     * @param user User id.
     * @return No content.
     */
    @RequestMapping(value = "/users/{user}/datapoints", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteDatapointsByUser(@PathVariable("user") String user) {
        datapointManagementService.deleteByUser(user);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
