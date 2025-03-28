package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.*;
import org.buildATrip.dao.LocationCodeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class AmadeusServiceImplTest {

    @Autowired
    AmadeusService amadeusService;

    @Autowired
    LocationCodeRepository locationCodeRepository;

    @BeforeEach
    void setUp() {
        //Bypass rate limit
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void getLocation() {
        try {
            LocationCode actual = amadeusService.getCityLocations("London");
            assertEquals("London", actual.getCityName());

            assertNotNull(locationCodeRepository.findById("LHR"));

        } catch (Exception e) {
            fail("Should not have thrown exception");
        }

    }

    @Test
    void getFlights() {
        try {
            LocationCode origin = amadeusService.getCityLocations("New York");
            LocationCode destination = amadeusService.getCityLocations("Paris");
            List<List<Flight>> flights = amadeusService.getFlights(origin.getCodeId(), destination.getCodeId(), LocalDate.parse("2026-01-02"), LocalDate.parse("2026-01-20"), 2, 2000, false);
            assertNotNull(flights);
            assertEquals(3, flights.size());

        } catch (Exception e) {
            fail("Should not have thrown an exception", e);
        }
    }

    @Test
    void getFlightsByDestination() {
        try {
            LocationCode origin = amadeusService.getCityLocations("New York");
            List<List<Flight>> flights = amadeusService.getFlightsByDestination(origin.getCodeId(), LocalDate.parse("2025-04-07"), 14, 2, 2000, false);
            assertNotNull(flights);
            assertEquals(3, flights.size());
        } catch (Exception e) {
            fail("Should not have thrown an exception", e);
        }
    }

    @Test
    void getFlightsByWrongDestination() {
        try {
            List<List<Flight>> flights = amadeusService.getFlightsByDestination("JKF", LocalDate.parse("2025-04-07"), 14, 2, 2000, false);
            fail("Should not have thrown an exception");
        } catch (ResponseException e) {
            return;
        }
        catch (Exception e) {
            fail("Should not have thrown an exception");
        }
    }

    @Test
    void getHotelsByCity() {
        try {
            List<Hotel> hotels = amadeusService.getHotelsByCity("PAR", 1, LocalDate.parse("2025-04-15"), LocalDate.parse("2025-04-18"), "3000", BoardType.ROOM_ONLY);
            assertNotNull(hotels);
        } catch (Exception e) {
            fail("Should not have thrown an exception", e);
        }
    }

    @Test
    void getActivitiesByCoordinates() {
        try {
            List<Activity> activities = amadeusService.getActivitiesByCoordinates(41.397158F, 2.160873F);
            assertNotNull(activities);
        } catch (Exception e) {
            fail("Should not have thrown an exception", e);
        }
    }
}