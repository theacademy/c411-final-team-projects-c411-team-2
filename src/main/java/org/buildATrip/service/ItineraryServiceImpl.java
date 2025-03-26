package org.buildATrip.service;

import org.buildATrip.entity.Itinerary;

import java.util.List;

public class ItineraryServiceImpl implements ItineraryService {
    @Override
    public Itinerary getItineraryById(int id) {
        return null;
    }

    @Override
    public Itinerary createItinerary(Itinerary itinerary) {
        return null;
    }

    @Override
    public Itinerary updateItinerary(int id, Itinerary itinerary) {
        return null;
    }

    @Override
    public void deleteItinerary(int id) {

    }

    @Override
    public Itinerary addFlightToItinerary(int itineraryId, int flightId) {
        return null;
    }

    @Override
    public Itinerary addHotelToItinerary(int itineraryId, int hotelId) {
        return null;
    }

    @Override
    public Itinerary addActivityToItinerary(int itineraryId, int activityId) {
        return null;
    }

    @Override
    public List<Itinerary> getItinerariesByUser(int userId) {
        return List.of();
    }
}
