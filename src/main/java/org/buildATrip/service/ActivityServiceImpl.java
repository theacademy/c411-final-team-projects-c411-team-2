package org.buildATrip.service;

import org.buildATrip.dao.ActivityRepository;
import org.buildATrip.entity.Activity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class ActivityServiceImpl implements ActivityService{
    @Autowired
    ActivityRepository activityRepository;

    @Override
    public List<Activity> searchActivities(String city, LocalDate startDate, LocalDate endDate) {
        return null;
    }

    @Override
    public Activity getActivityById(int id) {
        return null;
    }

    @Override
    public Activity createActivity(Activity activity) {
        return null;
    }

    @Override
    public Activity updateActivity(int id, Activity activity) {
        return null;
    }

    @Override
    public void deleteActivity(int id) {

    }
}
