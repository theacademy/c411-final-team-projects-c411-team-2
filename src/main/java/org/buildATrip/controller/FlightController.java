package org.buildATrip.controller;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.LocationCode;
import org.buildATrip.service.FlightService;
import org.buildATrip.service.LocationCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/flight")  // Changed from "/api"
@CrossOrigin(origins = "*")
public class FlightController {

    private final FlightService flightService;
    private final LocationCodeService locationCodeService;

    @Autowired
    public FlightController(FlightService flightService, LocationCodeService locationCodeService) {
        this.flightService = flightService;
        this.locationCodeService = locationCodeService;
    }

    // Get all flights
    @GetMapping("")  // Changed from "/flights"
    public ResponseEntity<List<Flight>> getAllFlights() {
        List<Flight> flights = flightService.getAllFlights();
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    // Get flight by ID
    @GetMapping("/{id}")  // Changed from "/flights/{id}"
    public ResponseEntity<Flight> getFlightById(@PathVariable Integer id) {
        Optional<Flight> flight = flightService.getFlightById(id);
        return flight.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Search for outbound flights
    @GetMapping("/search")  // Changed from "/flights/search"
    public ResponseEntity<List<List<Flight>>> searchFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "1000") int maxPrice,
            @RequestParam(defaultValue = "false") boolean nonStop) {

        try {
            List<List<Flight>> flightOptions = flightService.searchOutboundFlights(
                    origin, destination, departureDate, adults, maxPrice, nonStop);

            return new ResponseEntity<>(flightOptions, HttpStatus.OK);
        } catch (ResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching flights: " + e.getMessage(), e);
        }
    }

    // Search for return flights
    @GetMapping("/search/return")  // Changed from "/flights/search/return"
    public ResponseEntity<List<List<Flight>>> searchReturnFlights(
            @RequestParam String origin,
            @RequestParam String destination,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "1000") int maxPrice,
            @RequestParam(defaultValue = "false") boolean nonStop) {

        try {
            // For return flights, we swap origin and destination
            List<List<Flight>> flightOptions = flightService.searchReturnFlights(
                    origin, destination, returnDate, adults, maxPrice, nonStop);

            return new ResponseEntity<>(flightOptions, HttpStatus.OK);
        } catch (ResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching return flights: " + e.getMessage(), e);
        }
    }

    // Search for flight destinations
    @GetMapping("/destinations")  // Changed from "/flights/destinations"
    public ResponseEntity<List<List<Flight>>> searchFlightDestinations(
            @RequestParam String origin,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam(defaultValue = "7") int duration,
            @RequestParam(defaultValue = "1") int adults,
            @RequestParam(defaultValue = "1000") int maxPrice,
            @RequestParam(defaultValue = "false") boolean nonStop) {

        try {
            List<List<Flight>> flightOptions = flightService.searchFlightDestinations(
                    origin, departureDate, duration, adults, maxPrice, nonStop);

            return new ResponseEntity<>(flightOptions, HttpStatus.OK);
        } catch (ResponseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error searching flight destinations: " + e.getMessage(), e);
        }
    }

    // Save selected outbound flights to an itinerary
    @PostMapping("/outbound")  // Changed from "/flights/outbound"
    public ResponseEntity<List<Flight>> saveOutboundFlights(
            @RequestBody List<Flight> flights,
            @RequestParam(required = false) Integer itineraryId) {

        try {
            // Use a default itinerary ID for testing if none is provided
            Integer effectiveItineraryId = itineraryId != null ? itineraryId : 1;
            List<Flight> savedFlights = flightService.selectAndSaveOutboundFlights(flights, effectiveItineraryId);
            return new ResponseEntity<>(savedFlights, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error saving outbound flights: " + e.getMessage(), e);
        }
    }

    // Save selected return flights to an itinerary
    @PostMapping("/return")  // Changed from "/flights/return"
    public ResponseEntity<List<Flight>> saveReturnFlights(
            @RequestBody List<Flight> flights,
            @RequestParam Integer itineraryId) {

        try {
            List<Flight> savedFlights = flightService.selectAndSaveReturnFlights(flights, itineraryId);
            return new ResponseEntity<>(savedFlights, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error saving return flights: " + e.getMessage(), e);
        }
    }

    // Save both outbound and return flights to an itinerary
    @PostMapping("/complete")  // Changed from "/flights/complete"
    public ResponseEntity<Map<String, List<Flight>>> saveCompleteFlights(
            @RequestBody Map<String, List<Flight>> flightGroups,
            @RequestParam Integer itineraryId) {

        try {
            List<Flight> outboundFlights = flightGroups.get("outbound");
            List<Flight> returnFlights = flightGroups.get("return");

            List<Flight> allSavedFlights = flightService.saveFlightsToItinerary(
                    outboundFlights, returnFlights, itineraryId);

            // Separate saved flights into outbound and return for the response
            Map<String, List<Flight>> result = new HashMap<>();

            // In a complete implementation, we would need logic to separate the saved flights
            // into outbound and return based on their relationship with the itinerary
            // For now, just return all flights together
            result.put("flights", allSavedFlights);

            return new ResponseEntity<>(result, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error saving flights: " + e.getMessage(), e);
        }
    }

    // Delete a flight
    @DeleteMapping("/{id}")  // Changed from "/flights/{id}"
    public ResponseEntity<Void> deleteFlight(@PathVariable Integer id) {
        try {
            flightService.deleteFlight(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting flight: " + e.getMessage(), e);
        }
    }
//
//    // Search for location code by keyword
//    @GetMapping("/location/search")  // Changed from "/locations/search"
//    public ResponseEntity<LocationCode> searchLocationCode(@RequestParam String keyword) {
//        try {
//            LocationCode locationCode = locationCodeService.findOrCreateLocationCode(keyword);
//            return new ResponseEntity<>(locationCode, HttpStatus.OK);
//        } catch (ResponseException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
//                    "Error searching location: " + e.getMessage(), e);
//        }
//    }

    // Get all flights for an itinerary
    @GetMapping("/itinerary/{itineraryId}")
    public ResponseEntity<List<Flight>> getFlightsByItineraryId(@PathVariable Integer itineraryId) {
        try {
            List<Flight> flights = flightService.getFlightsByItineraryId(itineraryId);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving flights for itinerary: " + e.getMessage(), e);
        }
    }

    // Get outbound and return flights for an itinerary
    @GetMapping("/itinerary/{itineraryId}/trips")
    public ResponseEntity<Map<String, List<Flight>>> getOutboundAndReturnFlights(@PathVariable Integer itineraryId) {
        try {
            Map<String, List<Flight>> flights = flightService.getOutboundAndReturnFlightsByItineraryId(itineraryId);
            return new ResponseEntity<>(flights, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving trip flights for itinerary: " + e.getMessage(), e);
        }
    }

    // Update a flight
    @PutMapping("/{id}")
    public ResponseEntity<Flight> updateFlight(
            @PathVariable Integer id,
            @RequestBody Flight flight) {

        try {
            // Ensure the ID in the path matches the flight object
            if (!id.equals(flight.getFlightId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Flight ID in path does not match flight object");
            }

            // Verify the flight exists
            if (!flightService.getFlightById(id).isPresent()) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Save the updated flight
            Flight updatedFlight = flightService.saveFlight(flight);
            return new ResponseEntity<>(updatedFlight, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating flight: " + e.getMessage(), e);
        }
    }
}