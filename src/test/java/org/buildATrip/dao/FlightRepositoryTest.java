package org.buildATrip.dao;

import org.buildATrip.entity.Flight;
import org.buildATrip.entity.LocationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
public class FlightRepositoryTest {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private LocationCodeRepository locationCodeRepository;

    private LocationCode originCode;
    private LocationCode destCode;
    private Flight testFlight;

    @BeforeEach
    void setUp() {
        // Clean up previous test data
        flightRepository.deleteAll();
        locationCodeRepository.deleteAll();

        // Create test location codes
        originCode = new LocationCode("JFK", "New York");
        destCode = new LocationCode("LAX", "Los Angeles");

        locationCodeRepository.save(originCode);
        locationCodeRepository.save(destCode);

        // Create test flight
        testFlight = new Flight();
        testFlight.setPrice(new BigDecimal("299.99"));
        testFlight.setDuration(LocalTime.of(6, 0));
        testFlight.setDate(LocalDate.now());
        testFlight.setDepartureTime(LocalTime.of(10, 0));
        testFlight.setIsNonstop(true);
        testFlight.setOriginCode(originCode);
        testFlight.setDestinationCode(destCode);
        testFlight.setFlightType("OUTBOUND");

        // Save the test flight for use in tests
        testFlight = flightRepository.save(testFlight);
    }

    // CREATE TESTS
    @Test
    void testCreateFlight() {
        // Given
        Flight newFlight = new Flight();
        newFlight.setPrice(new BigDecimal("199.99"));
        newFlight.setDuration(LocalTime.of(5, 30));
        newFlight.setDate(LocalDate.now().plusDays(1));
        newFlight.setDepartureTime(LocalTime.of(14, 0));
        newFlight.setIsNonstop(false);
        newFlight.setOriginCode(originCode);
        newFlight.setDestinationCode(destCode);
        newFlight.setFlightType("OUTBOUND");


        // When
        Flight savedFlight = flightRepository.save(newFlight);

        // Then
        assertThat(savedFlight.getFlightId()).isNotNull();
        assertThat(savedFlight.getPrice()).isEqualTo(new BigDecimal("199.99"));
    }

    // READ TESTS
    @Test
    void testFindById() {
        // When
        Optional<Flight> found = flightRepository.findById(testFlight.getFlightId());

        // Then
        assertThat(found).isPresent();
        assertThat(found.get().getPrice()).isEqualTo(testFlight.getPrice());
    }

    @Test
    void testFindByOriginCodeAndDestinationCode() {
        // When
        List<Flight> found = flightRepository.findByOriginCodeAndDestinationCode(originCode, destCode);

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getFlightId()).isEqualTo(testFlight.getFlightId());
    }

    @Test
    void testFindByOriginCodeAndDestinationCodeAndDate() {
        // When
        List<Flight> found = flightRepository.findByOriginCodeAndDestinationCodeAndDate(
                originCode, destCode, LocalDate.now());

        // Then
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getFlightId()).isEqualTo(testFlight.getFlightId());
    }

    // UPDATE TESTS
    @Test
    void testUpdateFlight() {
        // Given
        testFlight.setPrice(new BigDecimal("349.99"));
        testFlight.setDuration(LocalTime.of(6, 30));

        // When
        Flight updatedFlight = flightRepository.save(testFlight);

        // Then
        Optional<Flight> found = flightRepository.findById(testFlight.getFlightId());
        assertThat(found).isPresent();
        assertThat(found.get().getPrice()).isEqualTo(new BigDecimal("349.99"));
        assertThat(found.get().getDuration()).isEqualTo(LocalTime.of(6, 30));
    }

    // DELETE TESTS
    @Test
    void testDeleteFlight() {
        // Given
        Integer flightId = testFlight.getFlightId();

        // When
        flightRepository.deleteById(flightId);

        // Then
        Optional<Flight> found = flightRepository.findById(flightId);
        assertThat(found).isEmpty();
    }

    // Test connecting flights (with nextFlightId)
    @Test
    void testConnectingFlights() {
        // Given
        Flight connectedFlight = new Flight();
        connectedFlight.setPrice(new BigDecimal("150.00"));
        connectedFlight.setDuration(LocalTime.of(2, 0));
        connectedFlight.setDate(LocalDate.now());
        connectedFlight.setDepartureTime(LocalTime.of(17, 0));
        connectedFlight.setIsNonstop(true);
        connectedFlight.setOriginCode(destCode); // LAX
        connectedFlight.setFlightType("OUTBOUND");


        // Create another location for the final destination
        LocationCode finalDest = new LocationCode("MIA", "Miami");
        locationCodeRepository.save(finalDest);
        connectedFlight.setDestinationCode(finalDest);

        Flight savedConnectedFlight = flightRepository.save(connectedFlight);

        // Set up the connection
        testFlight.setNextFlightId(savedConnectedFlight.getFlightId());
        testFlight.setIsNonstop(false);
        flightRepository.save(testFlight);

        // When - Test that we can retrieve the first flight
        Optional<Flight> firstFlight = flightRepository.findById(testFlight.getFlightId());

        // Then - Verify connection exists
        assertThat(firstFlight).isPresent();
        assertThat(firstFlight.get().getNextFlightId()).isEqualTo(savedConnectedFlight.getFlightId());
        assertThat(firstFlight.get().getIsNonstop()).isFalse();

        // When - Get the connected flight
        Optional<Flight> secondFlight = flightRepository.findById(firstFlight.get().getNextFlightId());

        // Then - Verify it's the right one
        assertThat(secondFlight).isPresent();
        assertThat(secondFlight.get().getDestinationCode().getCodeId()).isEqualTo("MIA");
    }
}