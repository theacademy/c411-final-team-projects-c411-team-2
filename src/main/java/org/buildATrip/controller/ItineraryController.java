package org.buildATrip.controller;

import org.buildATrip.entity.*;
import org.buildATrip.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/itinerary")
@CrossOrigin(origins = "*")
public class ItineraryController {

    private final ItineraryService itineraryService;
    private final HotelService hotelService;
    private final ActivityService activityService;
    private final UserService userService;

    @Autowired
    public ItineraryController(ItineraryService itineraryService,
                               HotelService hotelService,
                               ActivityService activityService, UserService userService) {
        this.itineraryService = itineraryService;
        this.hotelService = hotelService;
        this.activityService = activityService;
        this.userService = userService;
    }

    // Get itinerary by ID
    @GetMapping("/{id}")
    public ResponseEntity<Itinerary> getItineraryById(@PathVariable int id) {
        Itinerary itinerary = itineraryService.getItineraryById(id);
        if (itinerary != null) {
            return new ResponseEntity<>(itinerary, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Create a new itinerary
    @PostMapping("")
    public ResponseEntity<Itinerary> createItinerary(@RequestBody Itinerary itinerary) {
        try {
            Itinerary createdItinerary = itineraryService.createItinerary(itinerary);
            return new ResponseEntity<>(createdItinerary, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating itinerary: " + e.getMessage(), e);
        }
    }

    // Update an existing itinerary
    @PutMapping("/{id}")
    public ResponseEntity<Itinerary> updateItinerary(@PathVariable int id, @RequestBody Itinerary itinerary) {
        try {
            // Ensure the ID in the path matches the itinerary object
            if (itinerary.getId() == null) {
                itinerary.setId(id);
            } else if (itinerary.getId() != id) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Itinerary ID in path does not match itinerary object");
            }

            Itinerary updatedItinerary = itineraryService.updateItinerary(itinerary);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error updating itinerary: " + e.getMessage(), e);
        }
    }

    // Delete an itinerary
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItinerary(@PathVariable int id) {
        try {
            itineraryService.deleteItinerary(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error deleting itinerary: " + e.getMessage(), e);
        }
    }

    // Get all itineraries for a user
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Itinerary>> getItinerariesByUser(@PathVariable int userId) {
        try {
            List<Itinerary> itineraries = itineraryService.getItinerariesByUser(userId);
            return new ResponseEntity<>(itineraries, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error retrieving itineraries: " + e.getMessage(), e);
        }
    }

    // Add a flight to an itinerary - single flight version
    @PostMapping("/{itineraryId}/flight/{flightId}")
    public ResponseEntity<Itinerary> addFlightToItinerary(@PathVariable int itineraryId, @PathVariable int flightId) {
        try {
            Itinerary updatedItinerary = itineraryService.addFlightToItinerary(itineraryId, flightId);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding flight to itinerary: " + e.getMessage(), e);
        }
    }

    // Add flights to an itinerary - handles flight lists (outbound)
    @PostMapping("/{itineraryId}/flights/outbound")
    public ResponseEntity<Itinerary> addOutboundFlightsToItinerary(
            @PathVariable int itineraryId,
            @RequestBody List<Flight> flights) {
        try {
            // Delegate to the flight service which handles the complexity of connected flights
            // and then updates the itinerary
            FlightService flightService =
                    ((ItineraryServiceImpl)itineraryService).getFlightService();

            flightService.selectAndSaveOutboundFlights(flights, itineraryId);

            // Return the updated itinerary
            Itinerary updatedItinerary = itineraryService.getItineraryById(itineraryId);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding outbound flights to itinerary: " + e.getMessage(), e);
        }
    }

    // Add flights to an itinerary - handles flight lists (return)
    @PostMapping("/{itineraryId}/flights/return")
    public ResponseEntity<Itinerary> addReturnFlightsToItinerary(
            @PathVariable int itineraryId,
            @RequestBody List<Flight> flights) {
        try {
            // Delegate to the flight service which handles the complexity of connected flights
            // and then updates the itinerary
            FlightService flightService =
                    ((ItineraryServiceImpl)itineraryService).getFlightService();

            flightService.selectAndSaveReturnFlights(flights, itineraryId);

            // Return the updated itinerary
            Itinerary updatedItinerary = itineraryService.getItineraryById(itineraryId);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding return flights to itinerary: " + e.getMessage(), e);
        }
    }

    // Add both outbound and return flights to an itinerary
    @PostMapping("/{itineraryId}/flights/complete")
    public ResponseEntity<Itinerary> addCompleteFlightsToItinerary(
            @PathVariable int itineraryId,
            @RequestBody Map<String, List<Flight>> flightGroups) {
        try {
            List<Flight> outboundFlights = flightGroups.get("outbound");
            List<Flight> returnFlights = flightGroups.get("return");

            // Delegate to the flight service
            FlightService flightService =
                    ((ItineraryServiceImpl)itineraryService).getFlightService();

            flightService.saveFlightsToItinerary(outboundFlights, returnFlights, itineraryId);

            // Return the updated itinerary
            Itinerary updatedItinerary = itineraryService.getItineraryById(itineraryId);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding flights to itinerary: " + e.getMessage(), e);
        }
    }

    // Add a hotel to an itinerary
    @PostMapping("/{itineraryId}/hotel/{hotelId}")
    public ResponseEntity<Itinerary> addHotelToItinerary(@PathVariable int itineraryId, @PathVariable int hotelId) {
        try {
            Itinerary updatedItinerary = itineraryService.addHotelToItinerary(itineraryId, hotelId);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding hotel to itinerary: " + e.getMessage(), e);
        }
    }

    // Add an activity to an itinerary
    @PostMapping("/{itineraryId}/activity/{activityId}")
    public ResponseEntity<Itinerary> addActivityToItinerary(@PathVariable int itineraryId, @PathVariable int activityId) {
        try {
            Itinerary updatedItinerary = itineraryService.addActivityToItinerary(itineraryId, activityId);
            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error adding activity to itinerary: " + e.getMessage(), e);
        }
    }

    // Create a new itinerary with specific parameters
    @PostMapping("/create")
    public ResponseEntity<Itinerary> createNewItinerary(
            @RequestParam int userId,
            @RequestParam int numAdults,
            @RequestParam(required = false) BigDecimal priceRangeFlight,
            @RequestParam(required = false) BigDecimal priceRangeHotel,
            @RequestParam(required = false) BigDecimal priceRangeActivity,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            // First, validate that the user exists
            User user = userService.getUserById(userId);
            if (user == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Create new itinerary object with the provided parameters
            Itinerary newItinerary = new Itinerary();
            newItinerary.setNumAdults(numAdults);
            newItinerary.setPriceRangeFlight(priceRangeFlight);
            newItinerary.setPriceRangeHotel(priceRangeHotel);
            newItinerary.setPriceRangeActivity(priceRangeActivity);
            newItinerary.setConfirmed(false); // Default to not confirmed
            newItinerary.setStartDate(startDate);
            newItinerary.setEndDate(endDate);

            // Set the validated user from the database
            newItinerary.setUser(user);

            Itinerary createdItinerary = itineraryService.createItinerary(newItinerary);
            return new ResponseEntity<>(createdItinerary, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error creating itinerary: " + e.getMessage(), e);
        }
    }

    // Confirm an itinerary (finalize booking)
    @PutMapping("/{id}/confirm")
    public ResponseEntity<Itinerary> confirmItinerary(@PathVariable int id) {
        try {
            Itinerary itinerary = itineraryService.getItineraryById(id);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Set the itinerary as confirmed
            itinerary.setConfirmed(true);

            // Calculate total price based on flights, hotels, and activities
            BigDecimal totalPrice = calculateTotalPrice(itinerary);
            itinerary.setTotalPrice(totalPrice);

            Itinerary confirmedItinerary = itineraryService.updateItinerary(itinerary);
            return new ResponseEntity<>(confirmedItinerary, HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error confirming itinerary: " + e.getMessage(), e);
        }
    }

    // Helper method to calculate total price of an itinerary
    private BigDecimal calculateTotalPrice(Itinerary itinerary) {
        // This is a simplified calculation - in a real application, you would
        // need to sum up the actual prices of flights, hotels, and activities
        BigDecimal totalPrice = BigDecimal.ZERO;

        // Add flight prices
        if (itinerary.getFlightsList() != null && !itinerary.getFlightsList().isEmpty()) {
            for (Flight flight : itinerary.getFlightsList()) {
                if (flight.getPrice() != null) {
                    totalPrice = totalPrice.add(flight.getPrice());
                }
            }
        }

        // Add hotel prices
        if (itinerary.getHotelsList() != null && !itinerary.getHotelsList().isEmpty()) {
            for (Hotel hotel : itinerary.getHotelsList()) {
                if (hotel.getPrice() != null) {
                    // Multiply by number of nights (endDate - startDate)
                    long nights = itinerary.getStartDate().until(itinerary.getEndDate()).getDays();
                    BigDecimal hotelTotal = hotel.getPrice().multiply(BigDecimal.valueOf(nights));
                    totalPrice = totalPrice.add(hotelTotal);
                }
            }
        }

        // Add activity prices
        if (itinerary.getActivitiesList() != null && !itinerary.getActivitiesList().isEmpty()) {
            for (Activity activity : itinerary.getActivitiesList()) {
                if (activity.getPrice() != null) {
                    // Multiply by number of adults
                    BigDecimal activityTotal = activity.getPrice().multiply(BigDecimal.valueOf(itinerary.getNumAdults()));
                    totalPrice = totalPrice.add(activityTotal);
                }
            }
        }

        return totalPrice;
    }

    // Remove a flight from an itinerary
    @DeleteMapping("/{itineraryId}/flight/{flightId}")
    public ResponseEntity<Void> removeFlightFromItinerary(@PathVariable int itineraryId, @PathVariable int flightId) {
        try {
            ((ItineraryServiceImpl) itineraryService).removeFlightFromItinerary(itineraryId, flightId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error removing flight from itinerary: " + e.getMessage(), e);
        }
    }

    // Remove all flights of a certain type from an itinerary
    @DeleteMapping("/{itineraryId}/flights/{flightType}")
    public ResponseEntity<Void> removeFlightsByTypeFromItinerary(
            @PathVariable int itineraryId,
            @PathVariable String flightType) {
        try {
            Itinerary itinerary = itineraryService.getItineraryById(itineraryId);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // Filter flights by type
            List<Flight> flightsToRemove = itinerary.getFlightsList().stream()
                    .filter(f -> flightType.equalsIgnoreCase(f.getFlightType()))
                    .collect(Collectors.toList());

            // Remove each flight
            for (Flight flight : flightsToRemove) {
                ((ItineraryServiceImpl) itineraryService).removeFlightFromItinerary(
                        itineraryId, flight.getFlightId());
            }

            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error removing flights from itinerary: " + e.getMessage(), e);
        }
    }

    // Remove a hotel from an itinerary
    @DeleteMapping("/{itineraryId}/hotel/{hotelId}")
    public ResponseEntity<Void> removeHotelFromItinerary(@PathVariable int itineraryId, @PathVariable int hotelId) {
        try {
            ((ItineraryServiceImpl) itineraryService).removeHotelFromItinerary(itineraryId, hotelId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error removing hotel from itinerary: " + e.getMessage(), e);
        }
    }

    // Remove an activity from an itinerary
    @DeleteMapping("/{itineraryId}/activity/{activityId}")
    public ResponseEntity<Void> removeActivityFromItinerary(@PathVariable int itineraryId, @PathVariable int activityId) {
        try {
            ((ItineraryServiceImpl) itineraryService).removeActivityFromItinerary(itineraryId, activityId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error removing activity from itinerary: " + e.getMessage(), e);
        }
    }

    // Replace outbound flights in an itinerary
    @PutMapping("/{itineraryId}/flights/outbound")
    public ResponseEntity<Itinerary> replaceOutboundFlights(
            @PathVariable int itineraryId,
            @RequestBody List<Flight> newFlights) {
        try {
            // 1. Get the itinerary
            Itinerary itinerary = itineraryService.getItineraryById(itineraryId);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 2. Get current outbound flights to remove
            List<Flight> currentOutboundFlights = itinerary.getFlightsList().stream()
                    .filter(f -> "OUTBOUND".equals(f.getFlightType()))
                    .collect(Collectors.toList());

            // 3. Remove all existing outbound flights
            for (Flight flight : currentOutboundFlights) {
                ((ItineraryServiceImpl) itineraryService).removeFlightFromItinerary(
                        itineraryId, flight.getFlightId());
            }

            // 4. Add the new flights
            FlightService flightService = ((ItineraryServiceImpl) itineraryService).getFlightService();
            flightService.selectAndSaveOutboundFlights(newFlights, itineraryId);

            // 5. Return the updated itinerary
            return new ResponseEntity<>(itineraryService.getItineraryById(itineraryId), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error replacing outbound flights: " + e.getMessage(), e);
        }
    }

    // Replace return flights in an itinerary
    @PutMapping("/{itineraryId}/flights/return")
    public ResponseEntity<Itinerary> replaceReturnFlights(
            @PathVariable int itineraryId,
            @RequestBody List<Flight> newFlights) {
        try {
            // 1. Get the itinerary
            Itinerary itinerary = itineraryService.getItineraryById(itineraryId);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 2. Get current return flights to remove
            List<Flight> currentReturnFlights = itinerary.getFlightsList().stream()
                    .filter(f -> "RETURN".equals(f.getFlightType()))
                    .collect(Collectors.toList());

            // 3. Remove all existing return flights
            for (Flight flight : currentReturnFlights) {
                ((ItineraryServiceImpl) itineraryService).removeFlightFromItinerary(
                        itineraryId, flight.getFlightId());
            }

            // 4. Add the new flights
            FlightService flightService = ((ItineraryServiceImpl) itineraryService).getFlightService();
            flightService.selectAndSaveReturnFlights(newFlights, itineraryId);

            // 5. Return the updated itinerary
            return new ResponseEntity<>(itineraryService.getItineraryById(itineraryId), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error replacing return flights: " + e.getMessage(), e);
        }
    }

    // Replace all flights in an itinerary
    @PutMapping("/{itineraryId}/flights")
    public ResponseEntity<Itinerary> replaceAllFlights(
            @PathVariable int itineraryId,
            @RequestBody Map<String, List<Flight>> flightGroups) {
        try {
            // 1. Get the itinerary
            Itinerary itinerary = itineraryService.getItineraryById(itineraryId);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 2. Remove all existing flights
            List<Flight> currentFlights = new ArrayList<>(itinerary.getFlightsList());
            for (Flight flight : currentFlights) {
                ((ItineraryServiceImpl) itineraryService).removeFlightFromItinerary(
                        itineraryId, flight.getFlightId());
            }

            // 3. Add new outbound and return flights
            FlightService flightService = ((ItineraryServiceImpl) itineraryService).getFlightService();
            flightService.saveFlightsToItinerary(
                    flightGroups.get("outbound"),
                    flightGroups.get("return"),
                    itineraryId
            );

            // 4. Return updated itinerary
            return new ResponseEntity<>(itineraryService.getItineraryById(itineraryId), HttpStatus.OK);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error replacing all flights: " + e.getMessage(), e);
        }
    }

    // Replace a hotel in an itinerary
    @PutMapping("/{itineraryId}/hotel/{oldHotelId}")
    public ResponseEntity<Itinerary> replaceHotel(
            @PathVariable int itineraryId,
            @PathVariable int oldHotelId,
            @RequestBody Hotel newHotel) {
        try {
            // 1. Validate the itinerary and hotel exist
            Itinerary itinerary = itineraryService.getItineraryById(itineraryId);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 2. Remove the old hotel
            ((ItineraryServiceImpl) itineraryService).removeHotelFromItinerary(itineraryId, oldHotelId);

            // 3. Save the new hotel
            hotelService.createHotel(newHotel);

            // 4. Add the new hotel to the itinerary
            Itinerary updatedItinerary = itineraryService.addHotelToItinerary(itineraryId, newHotel.getHotel_id());

            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error replacing hotel: " + e.getMessage(), e);
        }
    }

    // Replace an activity in an itinerary
    @PutMapping("/{itineraryId}/activity/{oldActivityId}")
    public ResponseEntity<Itinerary> replaceActivity(
            @PathVariable int itineraryId,
            @PathVariable int oldActivityId,
            @RequestBody Activity newActivity) {
        try {
            // 1. Validate the itinerary and activity exist
            Itinerary itinerary = itineraryService.getItineraryById(itineraryId);
            if (itinerary == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            // 2. Remove the old activity
            ((ItineraryServiceImpl) itineraryService).removeActivityFromItinerary(itineraryId, oldActivityId);

            // 3. Save the new activity
            Activity savedActivity = activityService.createActivity(newActivity);

            // 4. Add the new activity to the itinerary
            Itinerary updatedItinerary = itineraryService.addActivityToItinerary(itineraryId, savedActivity.getId());

            return new ResponseEntity<>(updatedItinerary, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "Error replacing activity: " + e.getMessage(), e);
        }
    }
}