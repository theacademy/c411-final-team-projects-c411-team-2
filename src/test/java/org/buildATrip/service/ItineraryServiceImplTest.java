package org.buildATrip.service;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.buildATrip.entity.Itinerary;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class ItineraryServiceImplTest {

    @Autowired
    ItineraryService itineraryService;

    @Autowired
    HotelService hotelService;


    @Test
    void addHotelToItinenary() {
/*        Itinerary itinerary = new Itinerary();
        itinerary.setNumAdults(2);
        itinerary.setPriceRangeFlight(new BigDecimal("500.00"));
        itinerary.setPriceRangeHotel(new BigDecimal("300.00"));
        itinerary.setPriceRangeActivity(new BigDecimal("150.00"));
        itinerary.setConfirmed(false);
        itinerary.setTotalPrice(new BigDecimal("950.00"));
        itinerary.setStartDate(LocalDate.of(2025, 6, 10));
        itinerary.setEndDate(LocalDate.of(2025, 6, 20));
        itinerary.setHotelsList(new ArrayList<>());
        itinerary.setActivitiesList(new ArrayList<>());
        itinerary.setFlightsList(new ArrayList<>());

        Itinerary insertItinerary = itineraryService.createItinerary(itinerary);

        Hotel hotel = new Hotel();
        hotel.setHotel_id("ZZNCENVX");
        hotel.setName("HotelName");
        hotel.setPrice(new BigDecimal("150.00"));
        hotel.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel.setAddress("France");
        hotel.setLatitude(new BigDecimal("43.66"));
        hotel.setLongitude(new BigDecimal("7.21"));
        hotel.setBoardType(BoardType.BREAKFAST);

        hotelService.createHotel(hotel);

        itineraryService.addHotelToItinerary(insertItinerary.getId(), hotel.getHotel_id());
        assertEquals(itinerary.getHotelsList().size(), 1);
        assertEquals(hotel.getItineraryList().size(), 1);*/
    }


}