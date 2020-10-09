package org.factory.dst.api.controller;

import org.factory.dst.process.dto.AverageDto;
import org.factory.dst.process.service.StatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Statistics methods.
 */
@Controller
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
}
