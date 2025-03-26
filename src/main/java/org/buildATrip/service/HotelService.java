package org.buildATrip.service;

import org.buildATrip.entity.Hotel;

import java.time.LocalDate;
import java.util.List;

public interface HotelService {

    List<Hotel> searchHotel(String city, LocalDate checkIn, LocalDate checkOut);

    Hotel getHotelById(String id);

    void deleteHotel(String id);

}
