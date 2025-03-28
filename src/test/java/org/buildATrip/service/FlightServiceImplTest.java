package org.buildATrip.service;

import org.buildATrip.dao.FlightRepository;
import org.buildATrip.dao.LocationCodeRepository;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.LocationCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
@Import({FlightServiceImpl.class, AmadeusServiceImpl.class})
public class FlightServiceImplTest {

    @Autowired
    private FlightService flightService;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private LocationCodeRepository locationCodeRepository;

    private LocationCode nyc;
    private LocationCode lax;
    private LocationCode mia;
    private Flight nycToLax;
    private Flight miaToNyc;

    @BeforeEach
    public void setUp() {
        // Clear any existing data
        flightRepository.deleteAll();

        // Create and save location codes
        nyc = new LocationCode("NYC", "NEW YORK");
        lax = new LocationCode("LAX", "LOS ANGELES");
        mia = new LocationCode("MIA", "MIAMI");
        locationCodeRepository.saveAll(Arrays.asList(nyc, lax, mia));

        // Create some test flights
        nycToLax = new Flight();
        nycToLax.setOriginCode(nyc);
        nycToLax.setDestinationCode(lax);
        nycToLax.setDate(LocalDate.now().plusDays(7));
        nycToLax.setDepartureTime(LocalTime.of(8, 0));
        nycToLax.setDuration(LocalTime.of(6, 30));
        nycToLax.setPrice(new BigDecimal("350.00"));
        nycToLax.setIsNonstop(true);

        miaToNyc = new Flight();
        miaToNyc.setOriginCode(mia);
        miaToNyc.setDestinationCode(nyc);
        miaToNyc.setDate(LocalDate.now().plusDays(5));
        miaToNyc.setDepartureTime(LocalTime.of(10, 0));
        miaToNyc.setDuration(LocalTime.of(3, 15));
        miaToNyc.setPrice(new BigDecimal("275.00"));
        miaToNyc.setIsNonstop(true);

        // Save test flights
        nycToLax = flightRepository.save(nycToLax);
        miaToNyc = flightRepository.save(miaToNyc);
    }

    @Test
    public void testGetAllFlights() {
        // Act
        List<Flight> flights = flightService.getAllFlights();

        // Assert
        assertNotNull(flights);
        assertEquals(2, flights.size());
        assertTrue(flights.stream().anyMatch(f -> f.getFlightId().equals(nycToLax.getFlightId())));
        assertTrue(flights.stream().anyMatch(f -> f.getFlightId().equals(miaToNyc.getFlightId())));
    }

    @Test
    public void testGetFlightById() {
        // Act
        Optional<Flight> foundFlight = flightService.getFlightById(nycToLax.getFlightId());

        // Assert
        assertTrue(foundFlight.isPresent());
        assertEquals(nycToLax.getOriginCode().getCodeId(), foundFlight.get().getOriginCode().getCodeId());
        assertEquals(nycToLax.getDestinationCode().getCodeId(), foundFlight.get().getDestinationCode().getCodeId());
        assertEquals(nycToLax.getPrice(), foundFlight.get().getPrice());
    }

    @Test
    public void testSaveFlight() {
        // Arrange
        Flight chiToLax = new Flight();
        chiToLax.setOriginCode(new LocationCode("CHI", "CHICAGO"));
        chiToLax.setDestinationCode(lax);
        chiToLax.setDate(LocalDate.now().plusDays(10));
        chiToLax.setDepartureTime(LocalTime.of(9, 30));
        chiToLax.setDuration(LocalTime.of(4, 45));
        chiToLax.setPrice(new BigDecimal("310.00"));
        chiToLax.setIsNonstop(true);

        // Make sure to save the location code first
        locationCodeRepository.save(chiToLax.getOriginCode());

        // Act
        Flight savedFlight = flightService.saveFlight(chiToLax);

        // Assert
        assertNotNull(savedFlight.getFlightId());
        assertEquals("CHI", savedFlight.getOriginCode().getCodeId());
        assertEquals("LAX", savedFlight.getDestinationCode().getCodeId());
        assertEquals(new BigDecimal("310.00"), savedFlight.getPrice());
    }

    @Test
    public void testSaveConnectingFlights() {
        // Arrange
        Flight firstLeg = new Flight();
        firstLeg.setOriginCode(nyc);
        firstLeg.setDestinationCode(mia);
        firstLeg.setDate(LocalDate.now().plusDays(15));
        firstLeg.setDepartureTime(LocalTime.of(7, 0));
        firstLeg.setDuration(LocalTime.of(3, 0));
        firstLeg.setPrice(null); // Price is only on the last leg for connecting flights
        firstLeg.setIsNonstop(true);

        Flight secondLeg = new Flight();
        secondLeg.setOriginCode(mia);
        secondLeg.setDestinationCode(lax);
        secondLeg.setDate(LocalDate.now().plusDays(15));
        secondLeg.setDepartureTime(LocalTime.of(11, 30));
        secondLeg.setDuration(LocalTime.of(5, 45));
        secondLeg.setPrice(new BigDecimal("520.00")); // Total price on the last leg
        secondLeg.setIsNonstop(true);

        List<Flight> connectedFlights = Arrays.asList(firstLeg, secondLeg);

        // Act
        List<Flight> savedFlights = flightService.saveConnectingFlights(connectedFlights);

        // Assert
        assertEquals(2, savedFlights.size());
        assertNotNull(savedFlights.get(0).getFlightId());
        assertNotNull(savedFlights.get(1).getFlightId());
        assertEquals(savedFlights.get(1).getFlightId(), savedFlights.get(0).getNextFlightId());
        assertNull(savedFlights.get(0).getPrice());
        assertEquals(new BigDecimal("520.00"), savedFlights.get(1).getPrice());
    }

    @Test
    public void testFindFlightsByOriginAndDestination() {
        // Act
        List<Flight> flights = flightService.findFlightsByOriginAndDestination(nyc, lax);

        // Assert
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals(nycToLax.getFlightId(), flights.get(0).getFlightId());
    }

    @Test
    public void testFindFlightsByOriginDestinationAndDate() {
        // Act
        List<Flight> flights = flightService.findFlightsByOriginDestinationAndDate(
                mia, nyc, miaToNyc.getDate());

        // Assert
        assertNotNull(flights);
        assertEquals(1, flights.size());
        assertEquals(miaToNyc.getFlightId(), flights.get(0).getFlightId());
    }

    @Test
    public void testSelectAndSaveOutboundFlights() {
        // Arrange
        Flight firstLeg = new Flight();
        firstLeg.setOriginCode(nyc);
        firstLeg.setDestinationCode(mia);
        firstLeg.setDate(LocalDate.now().plusDays(20));
        firstLeg.setDepartureTime(LocalTime.of(8, 30));
        firstLeg.setDuration(LocalTime.of(3, 0));
        firstLeg.setPrice(null);
        firstLeg.setIsNonstop(true);

        Flight secondLeg = new Flight();
        secondLeg.setOriginCode(mia);
        secondLeg.setDestinationCode(lax);
        secondLeg.setDate(LocalDate.now().plusDays(20));
        secondLeg.setDepartureTime(LocalTime.of(12, 30));
        secondLeg.setDuration(LocalTime.of(5, 45));
        secondLeg.setPrice(new BigDecimal("495.00"));
        secondLeg.setIsNonstop(true);

        List<Flight> outboundFlights = Arrays.asList(firstLeg, secondLeg);

        // Act
        List<Flight> savedFlights = flightService.selectAndSaveOutboundFlights(outboundFlights, 1);

        // Assert
        assertEquals(2, savedFlights.size());
        assertNotNull(savedFlights.get(0).getFlightId());
        assertNotNull(savedFlights.get(1).getFlightId());
        assertEquals(savedFlights.get(1).getFlightId(), savedFlights.get(0).getNextFlightId());

        // In a future implementation, we would also check that the flights are linked to the itinerary
        // with the correct type (OUTBOUND)
    }

    // Additional tests for the remaining methods would follow the same pattern
}