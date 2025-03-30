package org.buildATrip.service;

import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HotelService {

    List<Hotel> searchHotel(String cityOriginCode, Integer numberAdults, LocalDate checkIn, LocalDate checkOut, String hotelBudget, BoardType boardType);

    Hotel getHotelById(Integer id);

    Hotel createHotel(Hotel hotel);

    void deleteHotel(Integer id);

    void deleteAllHotel();

    Hotel findHotelWithItineraryList(Integer id);

}
