package org.buildATrip.service;

import org.buildATrip.dao.ActivityRepository;
import org.buildATrip.dao.FlightRepository;
import org.buildATrip.dao.HotelRepo;
import org.buildATrip.dao.ItineraryRepo;
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

    @Autowired
    public ItineraryServiceImpl(ItineraryRepo itineraryRepo, HotelRepo hotelRepo, ActivityRepository activityRepo, FlightRepository flightRepo) {
        this.itineraryRepo = itineraryRepo;
        this.hotelRepo = hotelRepo;
        this.activityRepo = activityRepo;
        this.flightRepo = flightRepo;
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
                .orElseThrow(() -> new EntityNotFoundException());

        return null;
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

        currentItinerary.getHotelsList().add(newHotel); // after this method, List<Itinenary> from Hotel class should also be updated bc its a bidirectional

        // allowed to update @ManyToMany field
        if (newHotel.getItineraryList() == null) {
            newHotel.setItineraryList(new ArrayList<>());
        }
        newHotel.getItineraryList().add(currentItinerary);
        Itinerary updatedItinerary = itineraryRepo.save(currentItinerary);
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

        return itineraryRepo.save(currentItinerary);
    }

    @Override
    public List<Itinerary> getItinerariesByUser(int userId) {
        return List.of();
    }

    @Override
    public void deleteAllItinerary() {
        itineraryRepo.deleteAll();
    }
}
