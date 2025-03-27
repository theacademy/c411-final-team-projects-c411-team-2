package org.buildATrip.service;

import org.buildATrip.entity.Itinerary;

import java.util.List;

public interface ItineraryService {

    Itinerary getItineraryById(int id);

    Itinerary createItinerary(Itinerary itinerary);

    Itinerary updateItinerary(Itinerary itinerary);

    void deleteItinerary(int id);

    Itinerary addFlightToItinerary(int itineraryId, int flightId);

    Itinerary addHotelToItinerary(int itineraryId, String hotelId);

    Itinerary addActivityToItinerary(int itineraryId, int activityId);

    // Itinerary addPoisToItinerary(int itineraryId, int poisId);

    // Itinerary confirmBooking(int itineraryId);

    List<Itinerary> getItinerariesByUser(int userId);

    void deleteAllItinerary();


}
