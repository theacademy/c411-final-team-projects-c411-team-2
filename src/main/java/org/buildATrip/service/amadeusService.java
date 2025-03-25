package org.buildATrip.service;

import org.buildATrip.entity.*;

import java.time.LocalDate;

public interface amadeusService {


    Flight[] getFlights(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, LocalDate returnDate, int numberAdults, int maxPrice, boolean isNonStop );
    //https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-search/api-reference

    Flight[] getFlightsByDestination(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, LocalDate returnDate, int numberAdults, int maxPrice, boolean isNonStop );
    //https://developers.amadeus.com/self-service/category/flights/api-doc/flight-inspiration-search/api-reference

    Hotel[] getHotelsByCity(String cityCode, int numberAdults, LocalDate checkinDate, LocalDate checkoutDate, String priceRange, BoardType boardType);
    //https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-list/api-reference
    //https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-search/api-reference

    Activity[] getActivitiesByCoordinates(float latitude, float longitude);
    //LAURA - YOU HAVE TO FILTER BY PRICE/ CATEGORY
    //https://developers.amadeus.com/self-service/category/destination-experiences/api-doc/tours-and-activities/api-reference

    PointOfInterest[] getPointsOfInterestByCoordinates(float latitude, float longitude, ActivityType[] activityTypes);
    //https://developers.amadeus.com/self-service/category/destination-experiences/api-doc/points-of-interest/api-reference
}
