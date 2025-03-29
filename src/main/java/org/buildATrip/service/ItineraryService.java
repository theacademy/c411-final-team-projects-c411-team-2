package org.buildATrip.service;

import org.buildATrip.entity.Itinerary;

import java.util.List;

public interface ItineraryService {

    Itinerary getItineraryById(int id);

    Itinerary createItinerary(Itinerary itinerary);

    Itinerary updateItinerary(Itinerary itinerary);

    void deleteItinerary(int id);

    Itinerary addFlightToItinerary(int itineraryId, int flightId) throws InsufficientBudgetException;

    Itinerary addHotelToItinerary(int itineraryId, Integer hotelId) throws InsufficientBudgetException;

    Itinerary addActivityToItinerary(int itineraryId, int activityId) throws InsufficientBudgetException;

    // Itinerary addPoisToItinerary(int itineraryId, int poisId);

    // Itinerary confirmBooking(int itineraryId);

    List<Itinerary> getItinerariesByUser(int userId);

    void deleteAllItinerary();

    void removeFlightFromItinerary(int itineraryId, int flightId);

    void removeHotelFromItinerary(int itineraryId, int hotelId);

    void removeActivityFromItinerary(int itineraryId, int activityId);
}
