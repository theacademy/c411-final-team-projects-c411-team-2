package org.buildATrip.service;

import org.buildATrip.dao.HotelRepo;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class HotelServiceImpl implements HotelService {

    private HotelRepo hotelRepo;
    private AmadeusService amadeusService;

    @Autowired
    public HotelServiceImpl(AmadeusService amadeusService, HotelRepo hotelRepo) {
        this.amadeusService = amadeusService;
        this.hotelRepo = hotelRepo;
    }

    @Override
    public List<Hotel> searchHotel(String cityOriginCode, Integer numberAdults, LocalDate checkIn, LocalDate checkOut, String hotelBudget, BoardType boardType) {
        if(numberAdults == null) {
            numberAdults = 1;
        }
        return new ArrayList<>(List.of(amadeusService.getHotelsByCity(cityOriginCode, numberAdults, checkIn, checkOut, hotelBudget, boardType)));
    }

    @Override
    public Hotel getHotelById(String id) {
        return hotelRepo.findById(id).orElse(null);
    }

    @Override
    public void createHotel(Hotel hotel) {
        hotelRepo.save(hotel);
    }


    @Override
    public void deleteHotel(String id) {
        hotelRepo.deleteById(id);
    }
}
