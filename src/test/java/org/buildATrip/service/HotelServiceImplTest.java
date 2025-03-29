package org.buildATrip.service;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class HotelServiceImplTest {

    @Autowired
    HotelService hotelService;

    @BeforeEach
    public void setUp() {
        hotelService.deleteAllHotel();
    }

    @Test
    void searchHotel() {
        // TO DO
    }

    @Test
    void getHotelById() {
        Hotel hotel = new Hotel();
        hotel.setName("HotelName");
        hotel.setPrice(new BigDecimal("432.19"));
        hotel.setCheckinDate(LocalDate.parse("2026-01-22"));
        hotel.setCheckoutDate(LocalDate.parse("2026-01-25"));
        hotel.setAddress("France");
        hotel.setLatitude(new BigDecimal("23.16"));
        hotel.setLongitude(new BigDecimal("95.01"));
        hotel.setBoardType(BoardType.ALL_INCLUSIVE);

        try {
            Hotel hotelSavedWithId = hotelService.createHotel(hotel);
            Hotel shoudBeTOMPCATS = hotelService.getHotelById(hotelSavedWithId.getHotel_id());
            assertEquals(hotelSavedWithId.getHotel_id(), shoudBeTOMPCATS.getHotel_id(), "Id should match");
            assertEquals(hotelSavedWithId.getName(), shoudBeTOMPCATS.getName(), "Name should match");

        } catch (Exception e) {
            fail("Should not have thrown exception");
        }

    }

    @Test
    void createHotel() {
        Hotel hotel = new Hotel();
        hotel.setName("MTL hotel");
        hotel.setPrice(new BigDecimal("150.00"));
        hotel.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel.setAddress("France");
        hotel.setLatitude(new BigDecimal("43.66"));
        hotel.setLongitude(new BigDecimal("7.21"));
        hotel.setBoardType(BoardType.BREAKFAST);

        try {
            Hotel savedHotelWithId = hotelService.createHotel(hotel);
            int hotel_id = savedHotelWithId.getHotel_id();
            Hotel shoudBeMTLHotel = hotelService.getHotelById(hotel_id);
            assertEquals(hotel.getName(), shoudBeMTLHotel.getName(), "Name should match");
            assertEquals(hotel.getHotel_id(), shoudBeMTLHotel.getHotel_id(), "Id should match");

        } catch (Exception e) {
            fail("Should not have thrown exception");
        }

    }

    @Test
    void deleteHotel() {

        Hotel hotel = new Hotel();
        hotel.setName("Hotel Miami");
        hotel.setPrice(new BigDecimal("150.00"));
        hotel.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel.setAddress("France");
        hotel.setLatitude(new BigDecimal("43.66"));
        hotel.setLongitude(new BigDecimal("7.21"));
        hotel.setBoardType(BoardType.BREAKFAST);

        try {
            Hotel hotelSavedWithId = hotelService.createHotel(hotel);
            Hotel addedHotel = hotelService.getHotelById(hotelSavedWithId.getHotel_id());

            assertEquals(hotel.getHotel_id(), addedHotel.getHotel_id(), "Should be the same hotel id");
            int hotel_id = hotelSavedWithId.getHotel_id();
            hotelService.deleteHotel(hotel_id);
            Hotel shouldBeNull = hotelService.getHotelById(hotel_id);
            assertNull(shouldBeNull, "Hotel Miami should not exists");
        } catch (Exception e) {
            fail("Should not have thrown exception");
        }

    }
}