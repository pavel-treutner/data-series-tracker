package org.factory.dst.api.controller;

import org.factory.dst.process.dto.AverageDto;
import org.factory.dst.process.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

/**
 * Statistics methods.
 */
@RestController
public class StatisticsController {

    private final StatisticsService statisticsService;

    @Autowired
    public StatisticsController(StatisticsService statisticsService) {
        this.statisticsService = statisticsService;
    }

    /**
     * Get 15 minutes average values for a device.
     *
     * @param device Device id.
     * @return Computed averages.
     */
    @RequestMapping(value = "/statistics/devices/{device}/avg",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET)
    public ResponseEntity<List<AverageDto>> getAveragesForDevice(@PathVariable("device") String device) {
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getAveragesByDevice(device));
    }

    /**
     * Get 15 minutes moving average values for a device.
     *
     * @param device Device id.
     * @param windowSize Window size.
     * @return Computed averages.
     */
    @RequestMapping(value = "/statistics/devices/{device}/moving_avg",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET)
    public ResponseEntity<List<AverageDto>> getMovingAveragesForDevice(@PathVariable("device") String device,
                                                                       @Min(1) @RequestParam("window_size") int windowSize) {
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getMovingAveragesByDevice(device, windowSize));
    }

    /**
     * Get 15 minutes average values for a user.
     *
     * @param user User id.
     * @return Computed averages.
     */
    @RequestMapping(value = "/statistics/users/{user}/avg",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET)
    public ResponseEntity<List<AverageDto>> getAveragesForUser(@PathVariable("user") String user) {
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getAveragesByUser(user));
    }

    /**
     * Get 15 minutes moving average values for a user.
     *
     * @param user User id.
     * @param windowSize Window size.
     * @return Computed averages.
     */
    @RequestMapping(value = "/statistics/users/{user}/moving_avg",
            produces = { MediaType.APPLICATION_JSON_VALUE },
            method = RequestMethod.GET)
    public ResponseEntity<List<AverageDto>> getMovingAveragesForUser(@PathVariable("user") String user,
                                                                     @Min(1) @RequestParam("window_size") int windowSize) {
        return ResponseEntity.status(HttpStatus.OK).body(statisticsService.getMovingAveragesByUser(user, windowSize));
    }
}
