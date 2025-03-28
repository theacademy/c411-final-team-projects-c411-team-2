package org.buildATrip.controller;

import org.buildATrip.entity.Activity;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.Hotel;
import org.buildATrip.entity.Itinerary;
import org.buildATrip.service.FlightService;
import org.buildATrip.service.ItineraryService;
import org.buildATrip.service.ItineraryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/itinerary")
@CrossOrigin(origins = "*")
public class ItineraryController {

    private final ItineraryService itineraryService;

    @Autowired
    public ItineraryController(ItineraryService itineraryService) {
        this.itineraryService = itineraryService;
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
            List<org.buildATrip.entity.Flight> outboundFlights = flightGroups.get("outbound");
            List<org.buildATrip.entity.Flight> returnFlights = flightGroups.get("return");

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
    public ResponseEntity<Itinerary> addHotelToItinerary(@PathVariable int itineraryId, @PathVariable String hotelId) {
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
            // Create new itinerary object with the provided parameters
            Itinerary newItinerary = new Itinerary();
            newItinerary.setNumAdults(numAdults);
            newItinerary.setPriceRangeFlight(priceRangeFlight);
            newItinerary.setPriceRangeHotel(priceRangeHotel);
            newItinerary.setPriceRangeActivity(priceRangeActivity);
            newItinerary.setConfirmed(false); // Default to not confirmed
            newItinerary.setStartDate(startDate);
            newItinerary.setEndDate(endDate);

            // Create the user reference (assuming you have a method for this)
            // This would typically come from a UserService
            // For now, using a placeholder approach
            org.buildATrip.entity.User user = new org.buildATrip.entity.User();
            user.setId(userId);
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

    // Remove a hotel from an itinerary
    @DeleteMapping("/{itineraryId}/hotel/{hotelId}")
    public ResponseEntity<Void> removeHotelFromItinerary(@PathVariable int itineraryId, @PathVariable String hotelId) {
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
}