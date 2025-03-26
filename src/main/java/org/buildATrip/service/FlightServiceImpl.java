package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.dao.FlightRepository;
import org.buildATrip.dao.LocationCodeRepository;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.LocationCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final LocationCodeRepository locationCodeRepository;
    private final AmadeusService amadeusService;

    @Autowired
    public FlightServiceImpl(FlightRepository flightRepository,
                             LocationCodeRepository locationCodeRepo,
                             AmadeusService amadeusService) {
        this.flightRepository = flightRepository;
        this.locationCodeRepository = locationCodeRepo;
        this.amadeusService = amadeusService;
    }

    @Override
    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    @Override
    public Optional<Flight> getFlightById(Integer id) {
        return flightRepository.findById(id);
    }

    @Override
    public Flight saveFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    public List<Flight> saveAllFlights(List<Flight> flights) {
        return flightRepository.saveAll(flights);
    }

    @Override
    @Transactional
    public List<Flight> saveConnectingFlights(List<Flight> connectedFlights) {
        if (connectedFlights == null || connectedFlights.isEmpty()) {
            return new ArrayList<>();
        }

        // First save all flights to generate IDs
        List<Flight> savedFlights = saveAllFlights(connectedFlights);

        // Now establish nextFlightId relationships
        for (int i = 0; i < savedFlights.size() - 1; i++) {
            savedFlights.get(i).setNextFlightId(savedFlights.get(i + 1).getFlightId());
        }

        // Save the updated relationships
        return flightRepository.saveAll(savedFlights);
    }

    @Override
    public void deleteFlight(Integer id) {
        flightRepository.deleteById(id);
    }

    @Override
    public List<Flight> findFlightsByOriginAndDestination(LocationCode originCode, LocationCode destinationCode) {
        return flightRepository.findByOriginCodeAndDestinationCode(originCode, destinationCode);
    }

    @Override
    public List<Flight> findFlightsByOriginDestinationAndDate(LocationCode originCode, LocationCode destinationCode, LocalDate date) {
        return flightRepository.findByOriginCodeAndDestinationCodeAndDate(originCode, destinationCode, date);
    }

    @Override
    public List<List<Flight>> searchOutboundFlights(String originLocationCode, String destinationLocationCode,
                                                    LocalDate departureDate, int numberAdults,
                                                    int maxPrice, boolean isNonStop) throws ResponseException {
        // Call Amadeus to get one-way flights for the outbound journey
        return amadeusService.getFlights(originLocationCode, destinationLocationCode,
                departureDate, null, numberAdults, maxPrice, isNonStop);
    }

    @Override
    public List<List<Flight>> searchReturnFlights(String originLocationCode, String destinationLocationCode,
                                                  LocalDate returnDate, int numberAdults,
                                                  int maxPrice, boolean isNonStop) throws ResponseException {
        // Call Amadeus to get one-way flights for the return journey (swap origin/destination)
        return amadeusService.getFlights(destinationLocationCode, originLocationCode,
                returnDate, null, numberAdults, maxPrice, isNonStop);
    }

    @Override
    public List<List<Flight>> searchFlightDestinations(String originLocationCode,
                                                       LocalDate departureDate, int duration,
                                                       int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        return amadeusService.getFlightsByDestination(originLocationCode,
                departureDate, duration, numberAdults, maxPrice, isNonStop);
    }

    @Override
    @Transactional
    public List<Flight> selectAndSaveOutboundFlights(List<Flight> selectedFlights, Integer itineraryId) {
        // Save the flights with connections established
        List<Flight> savedFlights = saveConnectingFlights(selectedFlights);

        // In full implementation, I will link these flights to the itinerary here
        // itineraryService.addOutboundFlightsToItinerary(itineraryId, savedFlights);
        // or
        // itineraryService.addFlightsToItinerary(itineraryId, savedFlights, "OUTBOUND");

        return savedFlights;
    }

    @Override
    @Transactional
    public List<Flight> selectAndSaveReturnFlights(List<Flight> selectedFlights, Integer itineraryId) {
        // Save the flights with connections established
        List<Flight> savedFlights = saveConnectingFlights(selectedFlights);

        // In full implementation, I will link these flights to the itinerary here
        // itineraryService.addReturnFlightsToItinerary(itineraryId, savedFlights);
        // or
        // itineraryService.addFlightsToItinerary(itineraryId, savedFlights, "RETURN");

        return savedFlights;
    }

    @Override
    @Transactional
    public List<Flight> saveFlightsToItinerary(List<Flight> outboundFlights, List<Flight> returnFlights, Integer itineraryId) {
        List<Flight> allSavedFlights = new ArrayList<>();

        // Save outbound flights
        List<Flight> savedOutboundFlights = saveConnectingFlights(outboundFlights);
        allSavedFlights.addAll(savedOutboundFlights);

        // Save return flights
        if (returnFlights != null && !returnFlights.isEmpty()) {
            List<Flight> savedReturnFlights = saveConnectingFlights(returnFlights);
            allSavedFlights.addAll(savedReturnFlights);
        }

        // In full implementation, I will link these flights to the itinerary here
        // itineraryService.addFlightsToItinerary(itineraryId, savedOutboundFlights, "OUTBOUND");
        // itineraryService.addFlightsToItinerary(itineraryId, savedReturnFlights, "RETURN");

        return allSavedFlights;
    }
}