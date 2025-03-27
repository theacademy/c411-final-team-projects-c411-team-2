package org.buildATrip.controller;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.entity.Activity;
import org.buildATrip.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/activity")
public class ActivityController {
    private final ActivityService activityService;

    @Autowired
    public ActivityController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable int id) {
        Activity activity = activityService.getActivityById(id);
        if (activity != null) {
            return new ResponseEntity<Activity>(activity, HttpStatus.OK);
        } else {
            return new ResponseEntity<Activity>(activity, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping()
    public ResponseEntity<Void> createActivity(@RequestBody Activity activity) {
        Activity createdActivity = activityService.createActivity(activity);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable int id, @RequestBody Activity activity) {
        Activity updatedActivity = activityService.updateActivity(id, activity);
        return updatedActivity != null ? ResponseEntity.ok(updatedActivity) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable int id) {
        activityService.deleteActivity(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Activity>> searchActivities(@RequestParam float latitude,
                                                           @RequestParam float longitude,
                                                           @RequestParam BigDecimal budgetActivity) {

        try {
            List<Activity> activities = activityService.searchActivities(latitude, longitude, budgetActivity);
            return new ResponseEntity<>(activities, HttpStatus.OK);
        } catch (ResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching activities: " + e.getMessage(), e);
        }
    }



}
