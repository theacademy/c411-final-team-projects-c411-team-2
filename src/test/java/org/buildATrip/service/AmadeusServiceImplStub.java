package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AmadeusServiceImplStub implements AmadeusService{

    public static final String FLIGHT_TYPE_OUTBOUND = "OUTBOUND";
    public static final String FLIGHT_TYPE_RETURN = "RETURN";


    @Override
    public String getCityLocations(String keyword) throws ResponseException {
        return "";
    }

    @Override
    public List<List<Flight>> getFlights(String originaLocationCode, String destinationLocationCode, LocalDate departureDate, LocalDate returnDate, int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        if (originaLocationCode.equals("JFK")) {
            return List.of(
                    List.of(
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, new BigDecimal("200"), LocalTime.of(4, 0), LocalDate.parse("2025-05-05"),
                                    LocalTime.of(11, 0), true, null, new LocationCode("JFK", "NEW YORK"), new LocationCode("HEL", "HELENA"))
                    ),
                    List.of(
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, null, LocalTime.of(4, 0), LocalDate.parse("2025-05-05"),
                                    LocalTime.of(11, 0), true, null, new LocationCode("JFK", "NEW YORK"), new LocationCode("HEL", "HELENA"))
                            ,
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, new BigDecimal("300"), LocalTime.of(5, 0), LocalDate.parse("2025-05-05"),
                                    LocalTime.of(22, 0), true, null, new LocationCode("HEL", "HELENA"), new LocationCode("PAR", "PARIS")
                            )
                    ),
                    List.of((
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, new BigDecimal("3300"), LocalTime.of(8, 0), LocalDate.parse("2025-05-07"),
                                    LocalTime.of(1, 0), true, null, new LocationCode("JFK", "NEW YORK"), new LocationCode("LHR", "LONDON"))
                    ))

            );
        }
        return null;
    }

    @Override
    public List<List<Flight>> getFlightsByDestination(String originLocationCode, LocalDate departureDate, int duration, int numberAdults, int maxPrice, boolean isNonStop) throws ResponseException {
        if (originLocationCode.equals("JFK")){
            return List.of(
                    List.of(
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, new BigDecimal("200"), LocalTime.of(4,0), LocalDate.parse("2025-05-05"),
                                    LocalTime.of(11, 0), true, null, new LocationCode("JFK", "NEW YORK"), new LocationCode("HEL", "HELENA"))
                    ),
                    List.of(
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, null, LocalTime.of(4,0), LocalDate.parse("2025-05-05"),
                                    LocalTime.of(11, 0), true, null, new LocationCode("JFK", "NEW YORK"), new LocationCode("HEL", "HELENA"))
                            ,
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, new BigDecimal("300"), LocalTime.of(5,0), LocalDate.parse("2025-05-05"),
                                    LocalTime.of(22, 0), true, null, new LocationCode("HEL", "HELENA"), new LocationCode("PAR", "PARIS")
                            )
                    ),
                    List.of((
                            new Flight(null, FLIGHT_TYPE_OUTBOUND, new BigDecimal("3300"), LocalTime.of(8,0), LocalDate.parse("2025-05-07"),
                                    LocalTime.of(1, 0), true, null, new LocationCode("JFK", "NEW YORK"), new LocationCode("LHR", "LONDON"))
                    ))

            );
        }
        return null;
    }

    @Override
    public List<Hotel> getHotelsByCity(String cityCode, int numberAdults, LocalDate checkinDate, LocalDate checkoutDate, String priceRange, BoardType boardType) {
        return null;
    }

    @Override
    public List<Activity> getActivitiesByCoordinates(float latitude, float longitude) {
        return null;
    }
}
