package org.buildATrip.service;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.dao.LocationCodeRepository;
import org.buildATrip.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class ItineraryServiceImplTest {

    @Autowired
    ItineraryService itineraryService;

    @Autowired
    HotelService hotelService;

    @Autowired
    UserService userService;

    @Autowired
    ActivityService activityService;

    @Autowired
    FlightService flightService;

    @Autowired
    private LocationCodeRepository locationCodeRepository;

    @BeforeEach
    public void setUp() {
        hotelService.deleteAllHotel();
        activityService.deleteAllActivity();
        flightService.deleteAllFlight();
        locationCodeRepository.deleteAll();
        itineraryService.deleteAllItinerary();
        userService.deleteAllUser();
    }

    @Test
    void addHotelFlightActivityToItinerary() {

        Hotel hotel1 = new Hotel();
        hotel1.setName("HotelName");
        hotel1.setPrice(new BigDecimal("250.00"));
        hotel1.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel1.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel1.setAddress("France");
        hotel1.setLatitude(new BigDecimal("43.66"));
        hotel1.setLongitude(new BigDecimal("7.21"));
        hotel1.setBoardType(BoardType.BREAKFAST);

        Hotel hotelWithId = hotelService.createHotel(hotel1);

        LocationCode originCode = new LocationCode("JFK", "New York");
        LocationCode destCode = new LocationCode("LAX", "Los Angeles");

        locationCodeRepository.save(originCode);
        locationCodeRepository.save(destCode);

        Flight testFlight = new Flight();
        testFlight.setPrice(new BigDecimal("500.00"));
        testFlight.setDuration(LocalTime.of(6, 0));
        testFlight.setDate(LocalDate.now());
        testFlight.setDepartureTime(LocalTime.of(10, 0));
        testFlight.setIsNonstop(true);
        testFlight.setOriginCode(originCode);
        testFlight.setDestinationCode(destCode);
        testFlight.setFlightType("flightType");

        Flight flightWithId = flightService.saveFlight(testFlight);

        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("250.00"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity activityWithId = activityService.createActivity(activity);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("250.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);
        try {
            Itinerary updatedItinerary = itineraryService.addHotelToItinerary(insertItinerary.getId(), hotelWithId.getHotel_id());
            updatedItinerary = itineraryService.addFlightToItinerary(insertItinerary.getId(), flightWithId.getFlightId());
            updatedItinerary = itineraryService.addActivityToItinerary(insertItinerary.getId(), activityWithId.getId());
            assertEquals(new BigDecimal("1000.00"), updatedItinerary.getTotalPrice());
        } catch (InsufficientBudgetException ex) {
            fail("Should not have thrown an exception");
        }

    }

    @Test
    void addHotelFlightActivityToItineraryWithInsufficientBudget() {

        Hotel hotel1 = new Hotel();
        hotel1.setName("HotelName");
        hotel1.setPrice(new BigDecimal("250.00"));
        hotel1.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel1.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel1.setAddress("France");
        hotel1.setLatitude(new BigDecimal("43.66"));
        hotel1.setLongitude(new BigDecimal("7.21"));
        hotel1.setBoardType(BoardType.BREAKFAST);

        Hotel hotelWithId = hotelService.createHotel(hotel1);

        LocationCode originCode = new LocationCode("JFK", "New York");
        LocationCode destCode = new LocationCode("LAX", "Los Angeles");

        locationCodeRepository.save(originCode);
        locationCodeRepository.save(destCode);

        Flight testFlight = new Flight();
        testFlight.setPrice(new BigDecimal("500.00"));
        testFlight.setDuration(LocalTime.of(6, 0));
        testFlight.setDate(LocalDate.now());
        testFlight.setDepartureTime(LocalTime.of(10, 0));
        testFlight.setIsNonstop(true);
        testFlight.setOriginCode(originCode);
        testFlight.setDestinationCode(destCode);
        testFlight.setFlightType("flightType");

        Flight flightWithId = flightService.saveFlight(testFlight);

        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("250.00"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity activityWithId = activityService.createActivity(activity);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("100.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("24.06"));
        itinerary.setPriceRangeActivity(new BigDecimal("13.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);
        try {
            Itinerary updatedItinerary = itineraryService.addHotelToItinerary(insertItinerary.getId(), hotelWithId.getHotel_id());
            updatedItinerary = itineraryService.addFlightToItinerary(insertItinerary.getId(), flightWithId.getFlightId());
            updatedItinerary = itineraryService.addActivityToItinerary(insertItinerary.getId(), activityWithId.getId());
            fail("Should have thrown an exception");
        } catch (InsufficientBudgetException ex) {
            // expected to catch exception
        }

    }

    @Test
    void addHotelToItinerary() {

        Hotel hotel1 = new Hotel();
        hotel1.setName("HotelName");
        hotel1.setPrice(new BigDecimal("150.00"));
        hotel1.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel1.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel1.setAddress("France");
        hotel1.setLatitude(new BigDecimal("43.66"));
        hotel1.setLongitude(new BigDecimal("7.21"));
        hotel1.setBoardType(BoardType.BREAKFAST);

        Hotel savedHotel = hotelService.createHotel(hotel1);


        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("150.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);

        try {
            Itinerary updatedItinerary = itineraryService.addHotelToItinerary(insertItinerary.getId(), savedHotel.getHotel_id());
            Hotel updatedHotel = hotelService.findHotelWithItineraryList(savedHotel.getHotel_id());
            assertEquals(1, updatedItinerary.getHotelsList().size(), "Should contains only 1 hotel");
            assertEquals(1, updatedHotel.getItineraryList().size(), "Should contains 1 itinerary");
            assertEquals(hotel1.getPrice(), updatedItinerary.getTotalPrice());
        } catch (InsufficientBudgetException ex) {
            fail("Should not have thrown an exception");
        }

    }

    @Test
    void removeHotelFromItinerary() {

        Hotel hotel1 = new Hotel();
        hotel1.setName("HotelName");
        hotel1.setPrice(new BigDecimal("150.00"));
        hotel1.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel1.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel1.setAddress("France");
        hotel1.setLatitude(new BigDecimal("43.66"));
        hotel1.setLongitude(new BigDecimal("7.21"));
        hotel1.setBoardType(BoardType.BREAKFAST);

        Hotel savedHotel = hotelService.createHotel(hotel1);


        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("150.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);

        try {
            Itinerary updatedItinerary = itineraryService.addHotelToItinerary(insertItinerary.getId(), savedHotel.getHotel_id());
            Hotel updatedHotel = hotelService.findHotelWithItineraryList(savedHotel.getHotel_id());
            assertEquals(1, updatedItinerary.getHotelsList().size(), "Should contains only 1 hotel");
            assertEquals(1, updatedHotel.getItineraryList().size(), "Should contains 1 itinerary");
            assertEquals(hotel1.getPrice(), updatedItinerary.getTotalPrice(), "itinerary total price should be updated");
            itineraryService.removeHotelFromItinerary(updatedItinerary.getId(), savedHotel.getHotel_id());
            updatedItinerary = itineraryService.getItineraryById(updatedItinerary.getId());
            assertEquals(new BigDecimal("0.00"), updatedItinerary.getTotalPrice(), "itinerary total price should be 0");
        } catch (InsufficientBudgetException ex) {
            fail("Should not have thrown an exception");
        }

    }


    @Test
    void addFlightToItinerary() {

        LocationCode originCode = new LocationCode("JFK", "New York");
        LocationCode destCode = new LocationCode("LAX", "Los Angeles");

        locationCodeRepository.save(originCode);
        locationCodeRepository.save(destCode);

        Flight testFlight = new Flight();
        testFlight.setPrice(new BigDecimal("299.99"));
        testFlight.setDuration(LocalTime.of(6, 0));
        testFlight.setDate(LocalDate.now());
        testFlight.setDepartureTime(LocalTime.of(10, 0));
        testFlight.setIsNonstop(true);
        testFlight.setOriginCode(originCode);
        testFlight.setDestinationCode(destCode);
        testFlight.setFlightType("flightType");

        Flight flightWithId = flightService.saveFlight(testFlight);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("299.99"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("150.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);

        Itinerary updatedItinerary = null;
        try {
            updatedItinerary = itineraryService.addFlightToItinerary(insertItinerary.getId(), flightWithId.getFlightId());
            assertEquals(1, updatedItinerary.getFlightsList().size(), "Should contains only 1 flight");
        } catch (InsufficientBudgetException e) {
            fail("Should not have thrown an exception");
        }


    }

    @Test
    void removeFlightFromItinerary() {

        LocationCode originCode = new LocationCode("JFK", "New York");
        LocationCode destCode = new LocationCode("LAX", "Los Angeles");

        locationCodeRepository.save(originCode);
        locationCodeRepository.save(destCode);

        Flight testFlight = new Flight();
        testFlight.setPrice(new BigDecimal("299.99"));
        testFlight.setDuration(LocalTime.of(6, 0));
        testFlight.setDate(LocalDate.now());
        testFlight.setDepartureTime(LocalTime.of(10, 0));
        testFlight.setIsNonstop(true);
        testFlight.setOriginCode(originCode);
        testFlight.setDestinationCode(destCode);
        testFlight.setFlightType("flightType");

        Flight flightWithId = flightService.saveFlight(testFlight);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("299.99"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("150.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);

        try {
            Itinerary updatedItinerary = itineraryService.addFlightToItinerary(insertItinerary.getId(), flightWithId.getFlightId());
            assertEquals(1, updatedItinerary.getFlightsList().size(), "Should contains only 1 flight");
            assertEquals(flightWithId.getPrice(), updatedItinerary.getTotalPrice(), "itinerary total price should be updated");

            itineraryService.removeFlightFromItinerary(updatedItinerary.getId(), flightWithId.getFlightId());
            updatedItinerary = itineraryService.getItineraryById(updatedItinerary.getId());
            assertEquals(new BigDecimal("0.00"), updatedItinerary.getTotalPrice(), "itinerary total price should be 0");

        } catch (InsufficientBudgetException e) {
            fail("Should not have thrown an exception");
        }


    }

    @Test
    void addActivityToItinerary() {

        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity activityWithId = activityService.createActivity(activity);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("250.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);
        try {
            Itinerary updatedItinerary = itineraryService.addActivityToItinerary(insertItinerary.getId(), activityWithId.getId());
            assertEquals(1, updatedItinerary.getActivitiesList().size(), "Should contains only 1 activity");
        } catch (InsufficientBudgetException e) {
            fail("Should not have thrown an exception");
        }

    }

    @Test
    void removeActivityFromItinerary() {

        Activity activity = new Activity();
        activity.setName("Skydiving");
        activity.setDescription("Jump from a plane and experience freedom.");
        activity.setRating(4.8);
        activity.setPrice(new BigDecimal("200.0"));
        activity.setLatitude(new BigDecimal("40.7128"));
        activity.setLongitude(new BigDecimal("-74.0060"));

        Activity activityWithId = activityService.createActivity(activity);

        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("250.00"));
        itinerary.setConfirmed(false);
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);
        try {
            Itinerary updatedItinerary = itineraryService.addActivityToItinerary(insertItinerary.getId(), activityWithId.getId());
            assertEquals(1, updatedItinerary.getActivitiesList().size(), "Should contains only 1 activity");
            assertTrue(activityWithId.getPrice().compareTo(updatedItinerary.getTotalPrice()) == 0, "Itinerary price must be updated to include the activity cost");
            itineraryService.removeActivityFromItinerary(updatedItinerary.getId(), activityWithId.getId());
            updatedItinerary = itineraryService.getItineraryById(updatedItinerary.getId());
            assertEquals(new BigDecimal("0.00"), updatedItinerary.getTotalPrice(), "Itinerary price must be 0");
        } catch (InsufficientBudgetException e) {
            fail("Should not have thrown an exception");
        }

    }

    @Test
    void findItineraryByUserId() {
        User user1 = new User();
        user1.setFirstName("Peter");
        user1.setLastName("Parker");
        user1.setEmail("spiderman@example.com");
        user1.setPassword("spider123");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1990, 2, 11));

        User user1WithId = userService.registerUser(user1);

        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(1);
        itinerary.setPriceRangeFlight(new BigDecimal("900.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("560.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("250.00"));
        itinerary.setConfirmed(false);
        itinerary.setTotalPrice(new BigDecimal("0"));
        itinerary.setStartDate(LocalDate.of(2025, 7, 9));
        itinerary.setEndDate(LocalDate.of(2025, 7, 14));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());

        Itinerary secondItinerary = new Itinerary();
        secondItinerary.setNumAdults(1);
        secondItinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        secondItinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        secondItinerary.setPriceRangeActivity(new BigDecimal("200.00"));
        secondItinerary.setConfirmed(false);
        secondItinerary.setTotalPrice(new BigDecimal("0"));
        secondItinerary.setStartDate(LocalDate.of(2027, 2, 3));
        secondItinerary.setEndDate(LocalDate.of(2027, 2, 5));
        secondItinerary.setHotelsList(new ArrayList<>());
        secondItinerary.setActivitiesList(new ArrayList<>());
        secondItinerary.setFlightsList(new ArrayList<>());

        itinerary.setUser(user1WithId);
        secondItinerary.setUser(user1WithId);

        itineraryService.createItinerary(itinerary);
        itineraryService.createItinerary(secondItinerary);

        List<Itinerary> itineraryList = userService.getItinerariesForUser(user1WithId.getId());
        assertNotNull(itineraryList);
        assertEquals(2, itineraryList.size());
    }
}