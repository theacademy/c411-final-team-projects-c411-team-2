package org.buildATrip.service;

import org.buildATrip.dao.HotelRepo;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class HotelServiceImpl implements HotelService {

    @Autowired
    HotelRepo hotelRepo;

    @Autowired
    AmadeusService amadeusService;


    @Override
    public List<Hotel> searchHotel(String cityOriginCode, int numberAdults, LocalDate checkIn, LocalDate checkOut, String hotelBudget, BoardType boardType) {
        return new ArrayList<>(List.of(amadeusService.getHotelsByCity(cityOriginCode, numberAdults, checkIn, checkOut, hotelBudget, boardType)));
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
