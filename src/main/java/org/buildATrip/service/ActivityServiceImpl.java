package org.buildATrip.service;

import org.buildATrip.dao.ActivityRepository;
import org.buildATrip.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ActivityServiceImpl implements ActivityService{

    private final ActivityRepository activityRepository;
    private final AmadeusService amadeusService;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, AmadeusService amadeusService) {
        this.activityRepository = activityRepository;
        this.amadeusService = amadeusService;
    }

    @Override
    public List<Activity> searchActivities(float latitude, float longitude, BigDecimal activityBudget) {

        return amadeusService.getActivitiesByCoordinates(latitude, longitude)
                .stream().
                filter(activity -> activity.getPrice().compareTo(activityBudget) <= 0)
                .collect(Collectors.toList());
        //Activity[] getActivitiesByCoordinates(float latitude, float longitude)

    }

    @Override
    public Activity getActivityById(int id) {
        return activityRepository.findById(id).orElse(null);
    }

    @Override
    public Activity createActivity(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public Activity updateActivity(int id, Activity activity) {
        //check if the ids match, and if there is such an id to update in db
        //otherwise save() will create a new entry in db which we do not want
        if(id == activity.getId() && activityRepository.existsById(id)) {
            return activityRepository.save(activity);
        }
        return null;
    }

    @Override
    public void deleteActivity(int id) {
        activityRepository.deleteById(id);
    }
}
