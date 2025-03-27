package org.buildATrip.service;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.buildATrip.entity.Itinerary;
import org.buildATrip.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

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

    @BeforeEach
    public void setUp() {
        hotelService.deleteAllHotel();
        itineraryService.deleteAllItinerary();
        userService.deleteAllUser();
    }

    @Test
    void addHotelToItinenary() {

        Hotel hotel1 = new Hotel();
        hotel1.setHotel_id("YOOOYOOO");
        hotel1.setName("HotelName");
        hotel1.setPrice(new BigDecimal("150.00"));
        hotel1.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel1.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel1.setAddress("France");
        hotel1.setLatitude(new BigDecimal("43.66"));
        hotel1.setLongitude(new BigDecimal("7.21"));
        hotel1.setBoardType(BoardType.BREAKFAST);

        hotelService.createHotel(hotel1);


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
        itinerary.setTotalPrice(new BigDecimal("950.00"));
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());
        itinerary.setUser(user1WithId);

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);


        Itinerary updatedItinerary = itineraryService.addHotelToItinerary(insertItinerary.getId(), hotel1.getHotel_id());
        Hotel updatedHotel = hotelService.findHotelWithItineraryList(hotel1.getHotel_id()); //hotelService.getHotelById(hotel1.getHotel_id());

        assertEquals(1, updatedItinerary.getHotelsList().size(), "Should contains only 1 hotel");
        assertEquals(1, updatedHotel.getItineraryList().size(), "Should contains 1 itinenary");
    }

    
}