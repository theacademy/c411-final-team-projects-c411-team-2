package org.buildATrip.service;

import com.amadeus.resources.FlightDestination;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.shopping.FlightOffers;
import org.buildATrip.entity.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
                        .and("children", 0)
                        .and("infants", 0)
                        .and("nonStop", isNonStop)
                        .and("maxPrice", maxPrice)
                        .and("currencyCode", "CAD")
                        .and("max", 3));
        // the date on which the traveler will depart from the destination to return to the origin. If this parameter is not specified, only one-way itineraries are found. If this parameter is specified, only round-trip itineraries are found. Dates are specified in the ISO 8601 YYYY-MM-DD format, e.g. 2018-02-28
        for (FlightOfferSearch flightOffer : flightOffers) {
            flightOffer.getPrice();
            FlightOfferSearch.SearchSegment[] segmentsOneWay = flightOffer.getItineraries()[0].getSegments();
            for (FlightOfferSearch.SearchSegment segment : segmentsOneWay) {
                segment.getDeparture().getIataCode();
                LocalDateTime.parse(segment.getDeparture().getAt());
                LocalDateTime.parse(segment.getArrival().getIataCode());
                segment.getDuration();
            }
        }
        return null;
    }


    @Override
    public Flight[] getFlightsByDestination(String originLocationCode, String destinationLocationCode, LocalDate departureDate, int duration, int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        FlightDestination[] flightDestinations = amadeus.shopping.flightDestinations.get(
                Params.with("origin", originLocationCode)
                        .and("departureDate", departureDate)
                        .and("duration", duration)
                        .and("oneWay", false)
                        .and("nonStop", isNonStop)
                        .and("maxPrice", maxPrice)
                        .and("viewBy", "DESTINATION")

        );
        for ()
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

}