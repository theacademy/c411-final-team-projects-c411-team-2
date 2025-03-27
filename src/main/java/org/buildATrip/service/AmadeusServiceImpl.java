package org.buildATrip.service;

import com.amadeus.Response;
import com.amadeus.resources.FlightDestination;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.HotelOffer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.buildATrip.entity.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import org.buildATrip.dao.LocationCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmadeusServiceImpl implements AmadeusService {

    private final Amadeus amadeus;
    private LocationCodeRepo locationCodeRepository;

    @Autowired
    public AmadeusServiceImpl(Amadeus amadeus, LocationCodeRepo locationCodeRepository) {
        this.amadeus = amadeus;
        this.locationCodeRepository=locationCodeRepository;
    }

    public LocationCode getAirportLocations(String keyword) throws ResponseException {
        Location[] locations = amadeus.referenceData.locations.get(
                Params.with("keyword", keyword)
                        .and("subType", Locations.AIRPORT)
        );
        //Hardcoded first location!!
        LocationCode locationCode = new LocationCode(keyword, locations[0].getAddress().getCityName());
        locationCodeRepository.save(locationCode);
        return locationCode;
    }


    @Override
    public List<List<Flight>> getFlights(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, LocalDate returnDate, int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        FlightOfferSearch[] flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", originaLocationCode)
                        .and("destinationLocationCode", destinationLocationCode)
                        .and("departureDate", departureDate)
                       // .and("returnDate", returnDate)
                        .and("adults", numberAdults)
                        .and("children", 0)
                        .and("infants", 0)
                        .and("nonStop", isNonStop)
                        .and("maxPrice", maxPrice)
                        .and("currencyCode", "CAD")
                        .and("max", 3));    //max number of flights offer to return
        // the date on which the traveler will depart from the destination to return to the origin. If this parameter is not specified, only one-way itineraries are found. If this parameter is specified, only round-trip itineraries are found. Dates are specified in the ISO 8601 YYYY-MM-DD format, e.g. 2018-02-28
        List<List<Flight>> flights = new ArrayList();

        for (FlightOfferSearch flightOffer : flightOffers) {
            List<Flight> connectedFlights = new ArrayList();
            FlightOfferSearch.SearchSegment[] segmentsOneWay = flightOffer.getItineraries()[0].getSegments();
            int countNumberFlights = 0;

            for (FlightOfferSearch.SearchSegment segment : segmentsOneWay) {

                countNumberFlights++;
                Flight flight = new Flight();
                flight.setOriginCode(locationCodeRepository.findById(segment.getDeparture().getIataCode()).orElse(getAirportLocations(segment.getDeparture().getIataCode())));
                flight.setDate(LocalDateTime.parse(segment.getDeparture().getAt()).toLocalDate());
                flight.setDepartureTime(LocalDateTime.parse(segment.getDeparture().getAt()).toLocalTime());
                flight.setDestinationCode(locationCodeRepository.findById(segment.getArrival().getIataCode()).orElse(getAirportLocations(segment.getArrival().getIataCode())));
                Duration duration = Duration.parse(segment.getDuration());
                flight.setDuration(LocalTime.of((int) duration.toHours(), duration.toMinutesPart())); //don't think a flight can be more than 24h
                if(segmentsOneWay.length == countNumberFlights){
                    flight.setPrice(new BigDecimal(String.valueOf(flightOffer.getPrice().getGrandTotal())));
                }
                connectedFlights.add(flight);
            }
            flights.add(connectedFlights);

        }
        return flights;
    }


    @Override
    public List<List<Flight>> getFlightsByDestination(String originLocationCode, LocalDate departureDate, int duration, int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        FlightDestination[] flightDestinations = amadeus.shopping.flightDestinations.get(
                Params.with("origin", originLocationCode)
                        .and("departureDate", departureDate)
                        //.and("duration", duration)
                        .and("oneWay", true)
                        .and("nonStop", isNonStop)
                        .and("maxPrice", maxPrice)
                        .and("viewBy", "DESTINATION")

        );

        List<List<Flight>> flightOffersBasedOnDestination = new ArrayList();
        for (int i=0; i<3; i++){
            List<List<Flight>> flightOffers= getFlights(originLocationCode,
                    flightDestinations[i].getDestination(),
                    departureDate,
                    departureDate.plusDays(duration),
                    numberAdults, maxPrice, isNonStop);
            flightOffersBasedOnDestination.add(flightOffers.get(0));
            bypassRateLimit();

        }
        return flightOffersBasedOnDestination;
    }

    private void bypassRateLimit() {
        try {
            TimeUnit.MILLISECONDS.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Hotel> getHotelsByCity(String cityCode, int numberAdults, LocalDate checkinDate, LocalDate checkoutDate, String priceRange, BoardType boardType) throws ResponseException {

        Response response = amadeus.get("/v1/reference-data/locations/hotels/by-city",
                Params.with("cityCode", cityCode));

        ObjectMapper objectMapper = new ObjectMapper();
        List<String> hotelIds = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper.readTree(response.getBody());
            for (int i=0; i<5; i++) {
                hotelIds.add(rootNode.get("data").get(i).get("hotelId").asText());
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }


//        HotelOffer[] offers = amadeus.shopping.hotelOffers.get(
//                Params.with("hotelIds", hotelIds)
//                        .and("adult", numberAdults)
//                        .and("checkInDate", checkinDate)
//                        .and("checkOutDate", checkoutDate)
//                        .and("countryOfResidence", "CAN")
//                        .and("roomQuantity", 1)
//                        .and("price", priceRange)  //200-300 string!
//                        //.and("currency", "CAD")
//                        //.and("boardType", boardType)
//
//        );
        Response response2 = amadeus.get("/v3/shopping/hotel-offers",
                Params.with("hotelIds", hotelIds)
                        .and("adult", numberAdults)
                        .and("checkInDate", checkinDate)
                        .and("checkOutDate", checkoutDate)
                        //.and("countryOfResidence", "CAN")
                        .and("roomQuantity", 1)
                        .and("price", priceRange)  //200-300 string!
                        //.and("currency", "CAD")
                        .and("boardType", boardType)
        );
        ObjectMapper objectMapper2 = new ObjectMapper();
        List<Hotel> hotels = new ArrayList<>();

        try {
            JsonNode rootNode = objectMapper2.readTree(response2.getBody());
            //handle Amadeus or no result
            int maxIteration = (rootNode.get("data").size()<5)?rootNode.get("data").size(): 5;
            for (int i=0; i<maxIteration; i++) {
                if (rootNode.get("data").get(i).get("available").asText().equals("true")){
                    Hotel hotel = new Hotel();

                    hotel.setName(rootNode.get("data").get(i).get("hotel").get("name").asText());
                    //currency is not right, taking only the first offer by hotel
                    hotel.setPrice(new BigDecimal(rootNode.get("data").get(i).get("offers").get(0).get("price").get("total").asText()));
                    hotel.setCheckinDate(checkinDate);
                    hotel.setCheckoutDate(checkoutDate);
                    hotel.setLongitude(new BigDecimal(rootNode.get("data").get(i).get("hotel").get("longitude").asText()));
                    hotel.setLatitude(new BigDecimal(rootNode.get("data").get(i).get("hotel").get("latitude").asText()));
                    hotel.setBoardType(boardType);
                    //hotel.setAddress(rootNode.get("data").get(i).get);
                    hotels.add(hotel);

                }
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return hotels;
    }

    @Override
    public List<Activity> getActivitiesByCoordinates(float latitude, float longitude) throws ResponseException {
        List<Activity> activities = new ArrayList<>();
        com.amadeus.resources.Activity[] activitiesOffer = amadeus.shopping.activities.get(
                Params.with("longitude", longitude)
                        .and("latitude", latitude)
        );
        int maxIteration = (activitiesOffer.length<5)?activitiesOffer.length: 5;
        for (int i=0; i<maxIteration; i++) {
            Activity activity = new Activity();
            activity.setName(activitiesOffer[i].getName());
            activity.setPrice(new BigDecimal(String.valueOf(activitiesOffer[i].getPrice().getAmount()!=null?activitiesOffer[i].getPrice().getAmount():0)));
            if (activitiesOffer[i].getDescription()!=null){
                activity.setDescription(activitiesOffer[i].getDescription());
            }
            if (activitiesOffer[i].getRating()!=null){
                activity.setRating(Double.parseDouble(activitiesOffer[i].getRating()));
            }
            activity.setLatitude(new BigDecimal(String.valueOf(activitiesOffer[i].getGeoCode().getLatitude())));
            activity.setLongitude(new BigDecimal(String.valueOf(activitiesOffer[i].getGeoCode().getLongitude())));
        }
        return activities;
    }


}