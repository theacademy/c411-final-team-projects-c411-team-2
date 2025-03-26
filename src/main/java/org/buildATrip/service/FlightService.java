package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.LocationCode;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightService {

    /**
     * Get all flights from the database
     * @return List of all flights
     */
    List<Flight> getAllFlights();

    /**
     * Get a flight by its ID
     * @param id Flight ID
     * @return Optional containing the flight if found
     */
    Optional<Flight> getFlightById(Integer id);

    /**
     * Save a flight to the database
     * @param flight Flight to save
     * @return Saved flight with generated ID
     */
    Flight saveFlight(Flight flight);

    /**
     * Save multiple flights to the database
     * @param flights List of flights to save
     * @return List of saved flights with generated IDs
     */
    List<Flight> saveAllFlights(List<Flight> flights);

    /**
     * Save a list of connecting flights, establishing the next_flight_id relationships
     * @param connectedFlights List of flights in connection order
     * @return List of saved flights with relationships established
     */
    List<Flight> saveConnectingFlights(List<Flight> connectedFlights);

    /**
     * Delete a flight by its ID
     * @param id Flight ID to delete
     */
    void deleteFlight(Integer id);

    /**
     * Find flights by origin and destination location codes
     * @param originCode Origin location code
     * @param destinationCode Destination location code
     * @return List of matching flights
     */
    List<Flight> findFlightsByOriginAndDestination(LocationCode originCode, LocationCode destinationCode);

    /**
     * Find flights by origin, destination and date
     * @param originCode Origin location code
     * @param destinationCode Destination location code
     * @param date Travel date
     * @return List of matching flights
     */
    List<Flight> findFlightsByOriginDestinationAndDate(LocationCode originCode, LocationCode destinationCode, LocalDate date);

    /**
     * Search for outbound flight offers from Amadeus API
     * @param originLocationCode Origin airport code (e.g., "JFK")
     * @param destinationLocationCode Destination airport code (e.g., "LAX")
     * @param departureDate Departure date
     * @param numberAdults Number of adult passengers
     * @param maxPrice Maximum price (0 for no limit)
     * @param isNonStop Only non-stop flights
     * @return List of lists of flights, where inner lists represent connected flights
     * @throws ResponseException If there's an error with the Amadeus API
     */
    List<List<Flight>> searchOutboundFlights(String originLocationCode, String destinationLocationCode,
                                             LocalDate departureDate, int numberAdults,
                                             int maxPrice, boolean isNonStop) throws ResponseException;

    /**
     * Search for return flight offers from Amadeus API
     * @param originLocationCode Original origin airport code (e.g., "JFK")
     * @param destinationLocationCode Original destination airport code (e.g., "LAX")
     * @param returnDate Return date
     * @param numberAdults Number of adult passengers
     * @param maxPrice Maximum price (0 for no limit)
     * @param isNonStop Only non-stop flights
     * @return List of lists of flights, where inner lists represent connected flights
     * @throws ResponseException If there's an error with the Amadeus API
     */
    List<List<Flight>> searchReturnFlights(String originLocationCode, String destinationLocationCode,
                                           LocalDate returnDate, int numberAdults,
                                           int maxPrice, boolean isNonStop) throws ResponseException;

    /**
     * Search for flight destinations from a specific origin
     * @param originLocationCode Origin airport code (e.g., "JFK")
     * @param departureDate Departure date
     * @param duration Trip duration in days
     * @param numberAdults Number of adult passengers
     * @param maxPrice Maximum price (0 for no limit)
     * @param isNonStop Only non-stop flights
     * @return List of lists of flights to various destinations
     * @throws ResponseException If there's an error with the Amadeus API
     */
    List<List<Flight>> searchFlightDestinations(String originLocationCode,
                                                LocalDate departureDate, int duration,
                                                int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException;

    /**
     * Select and save outbound flights to an itinerary
     * @param selectedFlights The list of connected outbound flights selected by the user
     * @param itineraryId The ID of the itinerary to link these flights to
     * @return The saved flights with established connections
     */
    List<Flight> selectAndSaveOutboundFlights(List<Flight> selectedFlights, Integer itineraryId);

    /**
     * Select and save return flights to an itinerary
     * @param selectedFlights The list of connected return flights selected by the user
     * @param itineraryId The ID of the itinerary to link these flights to
     * @return The saved flights with established connections
     */
    List<Flight> selectAndSaveReturnFlights(List<Flight> selectedFlights, Integer itineraryId);

    /**
     * Save both outbound and return flights to an itinerary
     * @param outboundFlights The list of connected outbound flights
     * @param returnFlights The list of connected return flights
     * @param itineraryId The ID of the itinerary to link these flights to
     * @return All saved flights with established connections
     */
    List<Flight> saveFlightsToItinerary(List<Flight> outboundFlights, List<Flight> returnFlights, Integer itineraryId);
}