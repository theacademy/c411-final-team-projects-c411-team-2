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
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import org.buildATrip.dao.LocationCodeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AmadeusServiceImpl implements AmadeusService {

    private final Amadeus amadeus;
    private LocationCodeRepository locationCodeRepository;

    @Autowired
    public AmadeusServiceImpl(Amadeus amadeus, LocationCodeRepository locationCodeRepository) {
        this.amadeus = amadeus;
        this.locationCodeRepository = locationCodeRepository;
        // Debug log to verify amadeus is initialized
        System.out.println("Amadeus service initialized: " + (amadeus != null ? "Successfully" : "Failed"));
    }

    public LocationCode getAirportLocations(String keyword) throws ResponseException {
        System.out.println("===== DEBUG: getAirportLocations =====");
        System.out.println("Searching for airport with keyword: " + keyword);

        // First check if we already have this location code in our database
        Optional<LocationCode> existingLocation = locationCodeRepository.findById(keyword);
        if (existingLocation.isPresent()) {
            System.out.println("Found existing location in database: " + keyword);
            return existingLocation.get();
        }

        // Also try to find by city name in case keyword is a city
        LocationCode locationByCity = locationCodeRepository.findByCityNameIgnoreCase(keyword);
        if (locationByCity != null) {
            System.out.println("Found location by city name: " + keyword);
            return locationByCity;
        }

        try {
            System.out.println("Calling Amadeus API...");
            Location[] locations = amadeus.referenceData.locations.get(
                    Params.with("keyword", keyword)
                            .and("subType", Locations.AIRPORT)
            );

            System.out.println("API call completed");
            System.out.println("Results returned: " + (locations != null ? locations.length : "null"));

            if (locations == null || locations.length == 0) {
                System.out.println("No locations found for keyword: " + keyword);

                // Instead of throwing an exception, try to handle common airport codes
                if (isCommonAirportCode(keyword)) {
                    System.out.println("Using fallback for common airport: " + keyword);
                    return getCommonAirportFallback(keyword);
                }

                throw new RuntimeException("No airport locations found for keyword: " + keyword);
            }

            Location location = locations[0];
            System.out.println("First location found: " + location.getIataCode() +
                    " (" + location.getName() + ")");

            String cityName = "Unknown";
            if (location.getAddress() != null && location.getAddress().getCityName() != null) {
                cityName = location.getAddress().getCityName();
            } else if (location.getName() != null) {
                cityName = location.getName();
            }

            System.out.println("City name determined: " + cityName);

            LocationCode locationCode = new LocationCode(location.getIataCode(), cityName);
            locationCodeRepository.save(locationCode);
            System.out.println("LocationCode saved to repository");
            return locationCode;
        } catch (ResponseException e) {
            System.out.println("Amadeus API ResponseException: " + e.getMessage());
            if (e.getResponse() != null) {
                System.out.println("Response code: " + e.getResponse().getStatusCode());
                System.out.println("Response body: " + e.getResponse().getBody());
            }

            // If we hit rate limits and keyword looks like an airport code, use fallback
            if (e.getResponse() != null && e.getResponse().getStatusCode() == 429 &&
                    isCommonAirportCode(keyword)) {
                System.out.println("Rate limited but using fallback for: " + keyword);
                return getCommonAirportFallback(keyword);
            }

            throw e;
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            System.out.println("===== END DEBUG: getAirportLocations =====");
        }
    }

    // Helper method to check if a string looks like a common airport code (3 uppercase letters)
    private boolean isCommonAirportCode(String code) {
        return code != null && code.length() == 3 && code.matches("[A-Z]{3}");
    }

    // Provide fallback data for common airport codes
    private LocationCode getCommonAirportFallback(String code) {
        LocationCode locationCode;

        switch (code) {
            case "JFK":
                locationCode = new LocationCode("JFK", "New York");
                break;
            case "LHR":
                locationCode = new LocationCode("LHR", "London");
                break;
            case "LAX":
                locationCode = new LocationCode("LAX", "Los Angeles");
                break;
            case "CDG":
                locationCode = new LocationCode("CDG", "Paris");
                break;
            case "HND":
                locationCode = new LocationCode("HND", "Tokyo");
                break;
            case "YYZ":
                locationCode = new LocationCode("YYZ", "Toronto");
                break;
            case "SFO":
                locationCode = new LocationCode("SFO", "San Francisco");
                break;
            default:
                locationCode = new LocationCode(code, code + " City");
        }

        // Save this fallback location to the database to avoid future API calls
        locationCodeRepository.save(locationCode);
        return locationCode;
    }

    // Add a simple test method to verify API connectivity
    public boolean testAmadeusConnection() {
        System.out.println("===== DEBUG: Testing Amadeus API Connection =====");
        try {
            // Try a simple API call that should always return something
            Location[] locations = amadeus.referenceData.locations.get(
                    Params.with("keyword", "JFK")
                            .and("subType", Locations.AIRPORT)
            );

            boolean success = locations != null && locations.length > 0;
            System.out.println("Connection test " + (success ? "SUCCESSFUL" : "FAILED"));
            if (success) {
                System.out.println("Found " + locations.length + " locations for JFK");
            }
            return success;
        } catch (ResponseException e) {
            System.out.println("API Connection test FAILED");
            System.out.println("Error: " + e.getMessage());
            System.out.println("Status code: " + e.getResponse().getStatusCode());
            return false;
        } catch (Exception e) {
            System.out.println("API Connection test FAILED");
            System.out.println("Unexpected error: " + e.getClass().getName() + ": " + e.getMessage());
            return false;
        } finally {
            System.out.println("===== END DEBUG: Testing Amadeus API Connection =====");
        }
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
                flight.setDuration(LocalTime.of((int) duration.toHours(), duration.toMinutesPart()));

                // Set the isNonstop value
                flight.setIsNonstop(isNonStop);
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
    public List<List<Flight>> getFlightsByDestination(String originLocationCode,
                                                      LocalDate departureDate,
                                                      int duration,
                                                      int numberAdults,
                                                      int maxPrice,
                                                      boolean isNonStop) throws ResponseException {
        // Use flightInspirationSearch instead of flightDestinations
        FlightDestination[] inspirationResults = amadeus.shopping.flightDestinations.get(
                Params.with("origin", originLocationCode)
                        .and("departureDate", departureDate)
                        .and("maxPrice", maxPrice)
        );

        List<List<Flight>> flightOffersBasedOnDestination = new ArrayList<>();
        for (FlightDestination inspiration : inspirationResults) {
            try {
                // Get the return date by adding the duration to departure date
                LocalDate returnDate = departureDate.plusDays(duration);

                // Get flights for this destination
                List<List<Flight>> flightOffers = getFlights(
                        originLocationCode,
                        inspiration.getDestination(),
                        departureDate,
                        returnDate,
                        numberAdults,
                        maxPrice,
                        isNonStop
                );

                if (!flightOffers.isEmpty()) {
                    flightOffersBasedOnDestination.add(flightOffers.get(0));
                }

                // Add rate limit bypass
                bypassRateLimit();
            } catch (Exception e) {
                // Log and continue if we can't get flights for a specific destination
                System.out.println("Error getting flights for destination " +
                        inspiration.getDestination() + ": " + e.getMessage());
            }
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