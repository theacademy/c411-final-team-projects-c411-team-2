package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.dao.ActivityRepository;
import org.buildATrip.entity.Activity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class ActivityServiceImplTest {

    private final ActivityService activityService;

    public ActivityServiceImplTest() {
        ActivityRepository activityRepository = new ActivityRepositoryStub();
        AmadeusService amadeusService = new AmadeusServiceImplStub(); // Assuming a stub for AmadeusService
        activityService = new ActivityServiceImpl(activityRepository, amadeusService);
    }

    @Test
    void testCreateAndGetActivityById() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));


        activityService.createActivity(activity);
        Activity retrievedActivity = activityService.getActivityById(1);

        assertNotNull(retrievedActivity);
        assertEquals(activity.getId(), retrievedActivity.getId());
        assertEquals(activity.getName(), retrievedActivity.getName());
    }

    @Test
    void testUpdateActivitySuccess() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));
        activityService.createActivity(activity);

        activity.setName("Updated Hiking Tour");
        Activity updatedActivity = activityService.updateActivity(1, activity);

        assertNotNull(updatedActivity);
        assertEquals("Updated Hiking Tour", updatedActivity.getName());
    }

    @Test
    void testUpdateActivityFailure() {
        Activity activity = new Activity();
        activity.setId(999);
        activity.setName("Nonexistent Activity");
        activity.setDescription("An activity that does not exist.");
        activity.setRating(3.0);
        activity.setPrice(new BigDecimal("50.0"));
        activity.setLatitude(new BigDecimal("35.0000"));
        activity.setLongitude(new BigDecimal("-120.0000"));

        Activity updatedActivity = activityService.updateActivity(999, activity);

        assertNull(updatedActivity);
    }

    @Test
    void testDeleteActivity() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));
        activityService.createActivity(activity);
        activityService.createActivity(activity);

        activityService.deleteActivity(1);

        assertNull(activityService.getActivityById(1));
    }

    @Test
    void testSearchActivities() throws ResponseException {
        Activity activity1 = new Activity();
        activity1.setName("Skydiving");
        activity1.setDescription("Jump from a plane and experience freedom.");
        activity1.setRating(4.8);
        activity1.setPrice(new BigDecimal("200.0"));
        activity1.setLatitude(new BigDecimal("40.7128"));
        activity1.setLongitude(new BigDecimal("-74.0060"));

        Activity activity2 = new Activity();
        activity2.setName("Scuba Diving");
        activity2.setDescription("Explore the underwater world.");
        activity2.setRating(4.5);
        activity2.setPrice(new BigDecimal("150.0"));
        activity2.setLatitude(new BigDecimal("40.7128"));
        activity2.setLongitude(new BigDecimal("-74.0060"));

        activityService.createActivity(activity1);
        activityService.createActivity(activity2);

        List<Activity> activities = activityService.searchActivities(40.7128f, -74.0060f, new BigDecimal("180.0"));

        assertNotNull(activities);
        assertEquals(1 , activities.size());
        assertEquals("Scuba Diving", activities.get(0).getName());
    }
}