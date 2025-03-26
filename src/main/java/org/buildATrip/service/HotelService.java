package org.buildATrip.service;

import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface HotelService {

    List<Hotel> searchHotel(String cityOriginCode, int numberAdults, LocalDate checkIn, LocalDate checkOut, String hotelBudget, BoardType boardType);

    Hotel getHotelById(String id);

    void deleteHotel(String id);

}
