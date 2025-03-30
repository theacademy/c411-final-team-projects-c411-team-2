package org.buildATrip.service;

import org.buildATrip.dao.*;
import org.buildATrip.entity.Activity;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.Hotel;
import org.buildATrip.entity.Itinerary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class ItineraryServiceImpl implements ItineraryService {

    private ItineraryRepo itineraryRepo;
    private HotelRepo hotelRepo;
    private ActivityRepository activityRepo;
    private FlightRepository flightRepo;
    private UserRepository userRepo;
    private FlightService flightService;

    @Autowired
    public ItineraryServiceImpl(ItineraryRepo itineraryRepo, HotelRepo hotelRepo,
                                ActivityRepository activityRepo, FlightRepository flightRepo,
                                UserRepository userRepo, @Lazy FlightService flightService) {
        this.itineraryRepo = itineraryRepo;
        this.hotelRepo = hotelRepo;
        this.activityRepo = activityRepo;
        this.flightRepo = flightRepo;
        this.userRepo = userRepo;
        this.flightService = flightService;
    }

    // Getter for FlightService to be used by controller
    public FlightService getFlightService() {
        return this.flightService;
    }


    @Override
    public Itinerary getItineraryById(int id) {
        return itineraryRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Itinerary createItinerary(Itinerary itinerary) {
        if(itinerary.getPriceRangeActivity() == null) {
            itinerary.setPriceRangeActivity(new BigDecimal("999999.99"));
        }
        if(itinerary.getPriceRangeHotel() == null) {
            itinerary.setPriceRangeHotel(new BigDecimal("9999999.99"));
        }
        if(itinerary.getPriceRangeFlight() == null) {
            itinerary.setPriceRangeFlight(new BigDecimal("9999999.99"));
        }
        return itineraryRepo.save(itinerary);
    }

    @Override
    public Itinerary updateItinerary(Itinerary itinerary) {
        if(itinerary.getId() == null || !itineraryRepo.existsById(itinerary.getId())) {
            throw new EntityNotFoundException("Itinerary not found!");
        }
        return itineraryRepo.save(itinerary);
    }

    @Override
    public void deleteItinerary(int id) {
        itineraryRepo.deleteById(id);
    }

    @Override
    @Transactional
    public Itinerary addFlightToItinerary(int itineraryId, int flightId) throws InsufficientBudgetException {
        Itinerary currentItinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        Flight newFlight = flightRepo.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        if(currentItinerary.getFlightsList() == null) {
            currentItinerary.setFlightsList(new ArrayList<>());
        }

        currentItinerary.getFlightsList().add(newFlight);

        if (newFlight.getItineraryList() == null) {
            newFlight.setItineraryList(new ArrayList<>());
        }

        if(currentItinerary.getPriceRangeFlight().compareTo(newFlight.getPrice()) < 0) {
            throw new InsufficientBudgetException("Your flight budget is insufficient to cover the cost!");
        }

        currentItinerary.setTotalPrice(updateItineraryCost(currentItinerary.getTotalPrice(), newFlight.getPrice()));

        newFlight.getItineraryList().add(currentItinerary);
        Itinerary updatedItinerary = itineraryRepo.save(currentItinerary);
        // update @ManyToMany field
        flightRepo.save(newFlight);
        return updatedItinerary;
    }

    @Override
    @Transactional
    public Itinerary addHotelToItinerary(int itineraryId, Integer hotelId) throws InsufficientBudgetException {
        Itinerary currentItinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinenary not found"));
        Hotel newHotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        if(currentItinerary.getHotelsList() == null) {
            currentItinerary.setHotelsList(new ArrayList<>());
        }

        currentItinerary.getHotelsList().add(newHotel);

        if (newHotel.getItineraryList() == null) {
            newHotel.setItineraryList(new ArrayList<>());
        }

        if(currentItinerary.getPriceRangeHotel().compareTo(newHotel.getPrice()) < 0) {
            throw new InsufficientBudgetException("Your hotel budget is insufficient to cover the cost!");
        }

        currentItinerary.setTotalPrice(updateItineraryCost(currentItinerary.getTotalPrice(), newHotel.getPrice()));
        newHotel.getItineraryList().add(currentItinerary);
        Itinerary updatedItinerary = itineraryRepo.save(currentItinerary);
        // update @ManyToMany field
        hotelRepo.save(newHotel);
        return updatedItinerary;
    }

    @Override
    @Transactional
    public Itinerary addActivityToItinerary(int itineraryId, int activityId) throws InsufficientBudgetException {
        Itinerary currentItinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinenary not found"));
        Activity newActivity = activityRepo.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));

        if(currentItinerary.getActivitiesList() == null) {
            currentItinerary.setActivitiesList(new ArrayList<>());
        }

        currentItinerary.getActivitiesList().add(newActivity);

        if (newActivity.getItineraryList() == null) {
            newActivity.setItineraryList(new ArrayList<>());
        }

        if(currentItinerary.getPriceRangeActivity().compareTo(newActivity.getPrice()) < 0) {
            throw new InsufficientBudgetException("Your activity budget is insufficient to cover the cost!");
        }

        currentItinerary.setTotalPrice(updateItineraryCost(currentItinerary.getTotalPrice(), newActivity.getPrice()));

        newActivity.getItineraryList().add(currentItinerary);
        Itinerary updatedItinerary = itineraryRepo.save(currentItinerary);
        // update @ManyToMany field
        activityRepo.save(newActivity);
        return updatedItinerary;
    }

    @Override
    public List<Itinerary> getItinerariesByUser(int userId) {
        return userRepo.findItinerariesByUserId(userId);
    }

    @Override
    public void deleteAllItinerary() {
        itineraryRepo.deleteAll();
    }

    // Methods for removing resources from itineraries could be added here
    @Override
    @Transactional
    public void removeFlightFromItinerary(int itineraryId, int flightId) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        Flight flight = flightRepo.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        // Remove the relationship from both sides
        if (itinerary.getFlightsList() != null) {
            itinerary.getFlightsList().remove(flight);
        }

        if (flight.getItineraryList() != null) {
            flight.getItineraryList().remove(itinerary);
        }

        itinerary.setTotalPrice(updateItineraryCost(itinerary.getTotalPrice(), flight.getPrice().negate()));

        // Save both entities
        itineraryRepo.save(itinerary);
        flightRepo.save(flight);
    }

    @Override
    @Transactional
    public void removeHotelFromItinerary(int itineraryId, int hotelId) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        Hotel hotel = hotelRepo.findById(hotelId)
                .orElseThrow(() -> new EntityNotFoundException("Hotel not found"));

        // Remove the relationship from both sides
        if (itinerary.getHotelsList() != null) {
            itinerary.getHotelsList().remove(hotel);
        }

        if (hotel.getItineraryList() != null) {
            hotel.getItineraryList().remove(itinerary);
        }

        itinerary.setTotalPrice(updateItineraryCost(itinerary.getTotalPrice(), hotel.getPrice().negate()));

        // Save both entities
        itineraryRepo.save(itinerary);
        hotelRepo.save(hotel);
    }

    @Override
    @Transactional
    public void removeActivityFromItinerary(int itineraryId, int activityId) {
        Itinerary itinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinerary not found"));
        Activity activity = activityRepo.findById(activityId)
                .orElseThrow(() -> new EntityNotFoundException("Activity not found"));

        // Remove the relationship from both sides
        if (itinerary.getActivitiesList() != null) {
            itinerary.getActivitiesList().remove(activity);
        }

        if (activity.getItineraryList() != null) {
            activity.getItineraryList().remove(itinerary);
        }

        itinerary.setTotalPrice(updateItineraryCost(itinerary.getTotalPrice(), activity.getPrice().negate()));

        // Save both entities
        itineraryRepo.save(itinerary);
        activityRepo.save(activity);
    }

    private BigDecimal updateItineraryCost(BigDecimal currentTotal, BigDecimal cost) {
        return (currentTotal == null) ? cost.setScale(2, RoundingMode.HALF_UP) : currentTotal.add(cost).setScale(2, RoundingMode.HALF_UP);
    }
}