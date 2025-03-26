package org.buildATrip.service;

import org.buildATrip.dao.HotelRepo;
import org.buildATrip.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class HotelServiceImpl implements HotelService {

    @Autowired
    HotelRepo hotelRepo;

    @Override
    public List<Hotel> searchHotel(String city, LocalDate checkIn, LocalDate checkOut) {
        // implement amadeus endpoint
        return List.of();
    }

    @Override
    public Hotel getHotelById(String id) {
        return hotelRepo.findById(id).orElse(null);
    }


    @Override
    public void deleteHotel(String id) {
        hotelRepo.deleteById(id);
    }
}
