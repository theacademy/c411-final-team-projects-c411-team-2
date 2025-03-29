package org.buildATrip.dao;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
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
        Hotel expectedHotel = hotelRepo.findById(hotelSavedWithId.getHotel_id()).orElse(null);
        assertNotNull(expectedHotel, "Shoud not be null");
        assertEquals(hotelSavedWithId.getHotel_id(), expectedHotel.getHotel_id(), "Should be the same hotel id");
        assertEquals(hotelSavedWithId.getName(), expectedHotel.getName(), "Should be the same name");
        assertEquals(hotelSavedWithId.getAddress(), expectedHotel.getAddress(), "Should be the same address");

    }

    @Test
    public void testDeleteHotelById() {
        Hotel firstHotel = new Hotel();
        firstHotel.setName("HotelName1");
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

        Hotel shouldBeFirstHotel = hotelRepo.getById(firstHotel.getHotel_id());
        Hotel shouldBeSecondtHotel = hotelRepo.getById(secondHotel.getHotel_id());
        assertEquals(firstHotel.getHotel_id(), shouldBeFirstHotel.getHotel_id(), "Should be the hotel #1");
        assertEquals(secondHotel.getHotel_id(), shouldBeSecondtHotel.getHotel_id(), "Should be the hotel #2");

        hotelRepo.deleteById(firstHotel.getHotel_id());
        hotelRepo.deleteById(secondHotel.getHotel_id());
        List<Hotel> shouldBeEmpty = hotelRepo.findAll();
        assertEquals(0, shouldBeEmpty.size());


    }

}