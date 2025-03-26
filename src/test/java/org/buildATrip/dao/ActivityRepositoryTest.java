package org.buildATrip.dao;

import org.buildATrip.entity.Activity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ActivityRepositoryTest {
    @Autowired
    private ActivityRepository activityRepository;


    @BeforeEach
    public void setUp() {
        activityRepository.deleteAll();
    }

    @Test
    public void testSaveActivity() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity savedActivity = activityRepository.save(activity);

        assertNotNull(savedActivity.getId());
        assertEquals("Skydiving", savedActivity.getName());
        assertEquals("Jump from a plane and experience freedom.", savedActivity.getDescription());
        assertEquals(4.8, savedActivity.getRating());
        assertEquals(new BigDecimal("200.0"), savedActivity.getPrice());
        assertEquals(new BigDecimal("40.7128"), savedActivity.getLatitude());
        assertEquals(new BigDecimal("-74.0060"), savedActivity.getLongitude());
    }

    @Test
    public void testFindActivityById() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity savedActivity = activityRepository.save(activity);

        Optional<Activity> foundActivity = activityRepository.findById(savedActivity.getId());
        assertTrue(foundActivity.isPresent());
        assertEquals("Skydiving", foundActivity.get().getName());
    }

    @Test
    public void testFindActivityByIdNotFound() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity savedActivity = activityRepository.save(activity);

        Optional<Activity> foundActivity = activityRepository.findById(20);
        assertFalse(foundActivity.isPresent());
    }

    @Test
    public void testFindAllActivities() {
        Activity activity1 = new Activity();
        activity1.setName("Skydiving");
        activity1.setDescription("Jump from a plane and experience freedom.");
        activity1.setRating(4.8);
        activity1.setPrice(new BigDecimal("200.0"));
        activity1.setLatitude(new BigDecimal("40.7128"));
        activity1.setLongitude(new BigDecimal("-74.0060"));

        Activity activity2 = new Activity();
        activity2.setName("Hiking");
        activity2.setDescription("Up and up to the mountains.");
        activity2.setRating(2.8);
        activity2.setPrice(new BigDecimal("20.0"));
        activity2.setLatitude(new BigDecimal("48.7128"));
        activity2.setLongitude(new BigDecimal("-92.0060"));

        activityRepository.save(activity1);
        activityRepository.save(activity2);

        List<Activity> activities = activityRepository.findAll();
        assertEquals(2, activities.size());
    }

    @Test
    public void testFindAllActivitiesZero() {
        List<Activity> activities = activityRepository.findAll();
        assertTrue(activities.isEmpty());
    }

    @Test
    public void testDeleteActivity() {
        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));
        Activity savedActivity = activityRepository.save(activity);

        activityRepository.deleteById(savedActivity.getId());

        Optional<Activity> deletedActivity = activityRepository.findById(savedActivity.getId());
        assertFalse(deletedActivity.isPresent());
    }

    @Test
    public void testDeleteAll() {
        Activity activity1 = new Activity();
        activity1.setName("Skydiving");
        activity1.setDescription("Jump from a plane and experience freedom.");
        activity1.setRating(4.8);
        activity1.setPrice(new BigDecimal("200.0"));
        activity1.setLatitude(new BigDecimal("40.7128"));
        activity1.setLongitude(new BigDecimal("-74.0060"));

        Activity activity2 = new Activity();
        activity2.setName("Hiking");
        activity2.setDescription("Up and up to the mountains.");
        activity2.setRating(2.8);
        activity2.setPrice(new BigDecimal("20.0"));
        activity2.setLatitude(new BigDecimal("48.7128"));
        activity2.setLongitude(new BigDecimal("-92.0060"));

        activityRepository.save(activity1);
        activityRepository.save(activity2);

        activityRepository.deleteAll();

        assertTrue(activityRepository.findAll().isEmpty());
    }



}