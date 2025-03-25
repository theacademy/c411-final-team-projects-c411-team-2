package org.buildATrip.service;

import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.shopping.FlightOffers;
import org.buildATrip.entity.*;

import java.time.LocalDate;
import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmadeusServiceImpl implements AmadeusService {

    private final Amadeus amadeus;

    @Autowired
    public AmadeusServiceImpl(Amadeus amadeus) {
        this.amadeus = amadeus;
    }

    public Location[] getAirportLocations(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(
                Params.with("keyword", keyword)
                        .and("subType", Locations.AIRPORT)
        );
    }


    @Override
    public Flight[] getFlights(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, LocalDate returnDate, int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", originaLocationCode)
                        .and("destinationLocationCode", destinationLocationCode)
                        .and("departureDate", departureDate)
                        .and("returnDate", returnDate)
                        .and("adults", numberAdults)
                        .and("nonStop", isNonStop)
                        .and("maxPrice", maxPrice)
                        .and("currencyCode", "CAD")
                        .and("max", 3));
        return null;
    }


    @Override
    public Flight[] getFlightsByDestination(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, int duration, int numberAdults, int maxPrice, boolean isNonStop) {
        return new Flight[0];
    }

    @Override
    public Hotel[] getHotelsByCity(String cityCode, int numberAdults, LocalDate checkinDate, LocalDate checkoutDate, String priceRange, BoardType boardType) {
        return new Hotel[0];
    }

    @Override
    public Activity[] getActivitiesByCoordinates(float latitude, float longitude) {
        return new Activity[0];
    }

    @Override
    public PointOfInterest[] getPointsOfInterestByCoordinates(float latitude, float longitude, ActivityType[] activityTypes) {
        return new PointOfInterest[0];
    }

    //flightMapper

}
