package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.entity.Activity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ActivityService {
    public List<Activity> searchActivities(float latitude, float longitude, BigDecimal activityBudget) throws ResponseException;

    public Activity getActivityById(int id);

    public Activity createActivity(Activity activity);

    public Activity updateActivity(int id, Activity activity);

    public void deleteActivity(int id);
}
