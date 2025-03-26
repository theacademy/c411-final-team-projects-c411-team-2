package org.buildATrip.service;

import org.buildATrip.dao.ActivityRepository;
import org.buildATrip.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    private ActivityRepository activityRepository;
    @Autowired
    private AmadeusService amadeusService;


    @Override
    public List<Activity> searchActivities(String city, LocalDate startDate, LocalDate endDate) {

        //Activity[] getActivitiesByCoordinates(float latitude, float longitude)
        return List.of();
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
