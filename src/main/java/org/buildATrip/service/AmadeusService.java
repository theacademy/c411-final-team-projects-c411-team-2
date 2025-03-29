package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface AmadeusService {

    String getCityLocations(String keyword) throws ResponseException;

    List<List<Flight>> getFlights(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, LocalDate returnDate, int numberAdults, int maxPrice, boolean isNonStop ) throws ResponseException;
    //https://developers.amadeus.com/self-service/category/flights/api-doc/flight-offers-search/api-reference

    List<List<Flight>> getFlightsByDestination(String originLocationCode, LocalDate departureDate, int duration, int numberAdults, int maxPrice, boolean isNonStop ) throws ResponseException;
    //https://developers.amadeus.com/self-service/category/flights/api-doc/flight-inspiration-search/api-reference

    List<Hotel> getHotelsByCity(String cityCode, int numberAdults, LocalDate checkinDate, LocalDate checkoutDate, String priceRange, BoardType boardType) throws ResponseException;
    //https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-list/api-reference
    //https://developers.amadeus.com/self-service/category/hotels/api-doc/hotel-search/api-reference

    List<Activity> getActivitiesByCoordinates(float latitude, float longitude) throws ResponseException;
    //https://developers.amadeus.com/self-service/category/destination-experiences/api-doc/tours-and-activities/api-reference
}
