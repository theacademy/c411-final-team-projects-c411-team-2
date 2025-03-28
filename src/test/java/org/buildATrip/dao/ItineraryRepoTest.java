package org.buildATrip.dao;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.Activity;
import org.buildATrip.entity.Itinerary;
import org.buildATrip.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class ItineraryRepoTest {

    @Autowired
    private ItineraryRepo itineraryRepo;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private FlightRepository flightRepository;


    private Itinerary testItinerary;
    private User savedUser;
    private Activity activityWithId;

    @Autowired
    public ItineraryRepoTest(ItineraryRepo itineraryRepo, UserRepository userRepository, ActivityRepository activityRepository, FlightRepository flightRepository) {
        this.itineraryRepo = itineraryRepo;
        this.userRepository = userRepository;
        this.activityRepository = activityRepository;
        this.flightRepository = flightRepository;

    }

    @BeforeEach
    public void setUp() {

        itineraryRepo.deleteAll();
        userRepository.deleteAll();
        activityRepository.deleteAll();

        itineraryRepo.deleteAll();
        User user1 = new User( );
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));
        savedUser = userRepository.save(user1);

        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        activityWithId = activityRepository.save(activity);

        testItinerary = new Itinerary();
        testItinerary.setNumAdults(2);
        testItinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        testItinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        testItinerary.setPriceRangeActivity(new BigDecimal("150.00"));
        testItinerary.setConfirmed(false);
        testItinerary.setTotalPrice(new BigDecimal("950.00"));
        testItinerary.setStartDate(LocalDate.of(2025, 6, 10));
        testItinerary.setEndDate(LocalDate.of(2025, 6, 20));
        testItinerary.setHotelsList(new ArrayList<>());
        testItinerary.setActivitiesList(new ArrayList<>());
        testItinerary.setFlightsList(new ArrayList<>());
        testItinerary.setUser(savedUser);
    }

    @Test
    public void deleteAllAndGetAll() {
        //since we deleteAll from the setup
        List<Itinerary> nothing = itineraryRepo.findAll();
        assertEquals(new ArrayList<Itinerary>(), nothing);

    }

    @Test
    public void saveAndGetById() {

        Itinerary actual = itineraryRepo.save(testItinerary);
        testItinerary.setId(actual.getId());
        assertNotNull(actual);
        assertEquals(testItinerary, actual);

        Itinerary found = itineraryRepo.findById(actual.getId()).orElse(null);
        found.getActivitiesList().size();
        //assertEquals(testItinerary.getActivitiesList(), found.getActivitiesList());
        assertEquals(List.copyOf(testItinerary.getActivitiesList()), List.copyOf(found.getActivitiesList()));
    }

    @Test
    public void updateById() {
        Itinerary actual = itineraryRepo.save(testItinerary);
        int id = actual.getId();
        testItinerary.setId(id);

        testItinerary.setActivitiesList(List.of(activityWithId));
        Itinerary updated = itineraryRepo.save(testItinerary);

        assertNotNull(updated);
        assertNotNull(updated.getActivitiesList() );
        updated.getActivitiesList().size();
        assertEquals(List.copyOf(testItinerary.getActivitiesList()), List.copyOf(updated.getActivitiesList()));


    }

    @Test
    public void deleteById() {
        Itinerary actual = itineraryRepo.save(testItinerary);
        int id = actual.getId();

        itineraryRepo.deleteById(id);
        boolean deleted = itineraryRepo.existsById(id);
        assertFalse(deleted);

    }


}