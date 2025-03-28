package org.buildATrip.service;

import org.buildATrip.dao.*;
import org.buildATrip.entity.Activity;
import org.buildATrip.entity.Flight;
import org.buildATrip.entity.Hotel;
import org.buildATrip.entity.Itinerary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ItineraryServiceImpl implements ItineraryService {


    private ItineraryRepo itineraryRepo;
    private HotelRepo hotelRepo;
    private ActivityRepository activityRepo;
    private FlightRepository flightRepo;
    private UserRepository userRepo;

    @Autowired
    public ItineraryServiceImpl(ItineraryRepo itineraryRepo, HotelRepo hotelRepo, ActivityRepository activityRepo, FlightRepository flightRepo, UserRepository userRepo) {
        this.itineraryRepo = itineraryRepo;
        this.hotelRepo = hotelRepo;
        this.activityRepo = activityRepo;
        this.flightRepo = flightRepo;
        this.userRepo = userRepo;
    }


    @Override
    public Itinerary getItineraryById(int id) {
        return itineraryRepo.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Itinerary createItinerary(Itinerary itinerary) {
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
    public Itinerary addFlightToItinerary(int itineraryId, int flightId) {
        Itinerary currentItinerary = itineraryRepo.findById(itineraryId)
                .orElseThrow(() -> new EntityNotFoundException("Itinenary not found"));
        Flight newFlight = flightRepo.findById(flightId)
                .orElseThrow(() -> new EntityNotFoundException("Flight not found"));

        if(currentItinerary.getFlightsList() == null) {
            currentItinerary.setFlightsList(new ArrayList<>());
        }

        currentItinerary.getFlightsList().add(newFlight);

        if (newFlight.getItineraryList() == null) {
            newFlight.setItineraryList(new ArrayList<>());
        }
        newFlight.getItineraryList().add(currentItinerary);
        Itinerary updatedItinerary = itineraryRepo.save(currentItinerary);
        // update @ManyToMany field
        flightRepo.save(newFlight);
        return updatedItinerary;
    }

    @Override
    @Transactional
    public Itinerary addHotelToItinerary(int itineraryId, String hotelId) {
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
        newHotel.getItineraryList().add(currentItinerary);
        Itinerary updatedItinerary = itineraryRepo.save(currentItinerary);
        // update @ManyToMany field
        hotelRepo.save(newHotel);
        return updatedItinerary;
    }

    @Override
    @Transactional
    public Itinerary addActivityToItinerary(int itineraryId, int activityId) {
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
}
