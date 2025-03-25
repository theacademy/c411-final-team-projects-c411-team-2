package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.TestApplicationConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class AmadeusServiceImplTest {

    @Autowired
    AmadeusService amadeusService;

    @BeforeEach
    void setUp() {
    }


    @Test
    void getFlights() {
        try {
            amadeusService.getFlights("NYC", "PAR", LocalDate.parse("2026-01-02"), LocalDate.parse("2026-01-20"), 2, 2000, false);

        }catch (Exception e){
            fail("Should not have thrown an exception");
        }
    }
    @Test
    void getFlightsByDestination() {
    }

    @Test
    void getHotelsByCity() {
    }

    @Test
    void getActivitiesByCoordinates() {
    }

    @Test
    void getPointsOfInterestByCoordinates() {
    }
}