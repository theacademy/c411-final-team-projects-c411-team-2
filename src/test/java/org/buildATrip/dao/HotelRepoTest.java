package org.buildATrip.dao;

import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.properties")
class HotelRepoTest {


    @Autowired
    private HotelRepo hotelRepo;

    @BeforeEach
    public void setUp() {
        hotelRepo.deleteAll();
    }

    @Test
    public void testGetHotelById() {
        Hotel hotel = new Hotel();
        hotel.setName("THIRD");
        hotel.setPrice(new BigDecimal("150.00"));
        hotel.setCheckinDate(LocalDate.parse("2025-06-10"));
        hotel.setCheckoutDate(LocalDate.parse("2025-06-15"));
        hotel.setAddress("France");
        hotel.setLatitude(new BigDecimal("43.66"));
        hotel.setLongitude(new BigDecimal("7.21"));
        hotel.setBoardType(BoardType.BREAKFAST);

        Hotel hotelSavedWithId = hotelRepo.save(hotel);
        Hotel expectedHotel = hotelRepo.getById(1);
        assertEquals(hotelSavedWithId, expectedHotel, "Should be the same hotel obj");

    }

/*    @Test
    public void testDeleteHotelById() {
        Hotel firstHotel = new Hotel();
        firstHotel.setPrice(new BigDecimal("150.00"));
        firstHotel.setCheckinDate(LocalDate.parse("2025-06-10"));
        firstHotel.setCheckoutDate(LocalDate.parse("2025-06-15"));
        firstHotel.setAddress("France");
        firstHotel.setLatitude(new BigDecimal("43.66"));
        firstHotel.setLongitude(new BigDecimal("7.21"));
        firstHotel.setBoardType(BoardType.BREAKFAST);

        Hotel secondHotel = new Hotel();
        secondHotel.setName("HotelName2");
        secondHotel.setPrice(new BigDecimal("499.99"));
        secondHotel.setCheckinDate(LocalDate.parse("2025-07-11"));
        secondHotel.setCheckoutDate(LocalDate.parse("2025-07-19"));
        secondHotel.setAddress("Italy");
        secondHotel.setLatitude(new BigDecimal("13.92"));
        secondHotel.setLongitude(new BigDecimal("16.20"));
        secondHotel.setBoardType(BoardType.FULL_BOARD);

        firstHotel = hotelRepo.save(firstHotel);
        secondHotel = hotelRepo.save(secondHotel);

        Hotel shouldBeFirstHotel = hotelRepo.getById(1);
        Hotel shouldBeSecondtHotel = hotelRepo.getById(2);
        assertEquals(firstHotel, shouldBeFirstHotel, "Should be the hotel, id=1");
        assertEquals(secondHotel, shouldBeSecondtHotel, "Should be the hotel, id=2");


    }*/

}