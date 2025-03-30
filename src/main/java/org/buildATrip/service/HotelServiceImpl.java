package org.buildATrip.service;

import com.amadeus.exceptions.ResponseException;
import org.buildATrip.dao.HotelRepo;
import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
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
        if(checkIn != null && checkOut == null) {
            checkOut = checkIn.plusDays(1);
        }

        try {
            return new ArrayList<>(amadeusService.getHotelsByCity(cityOriginCode, numberAdults, checkIn, checkOut, "0-" + hotelBudget, boardType));
        } catch (ResponseException ignored) {
            return new ArrayList<>();
        }
    }

    @Override
    public Hotel getHotelById(Integer id) {
        return hotelRepo.findById(id).orElse(null);
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelRepo.save(hotel);
    }


    @Override
    public void deleteHotel(Integer id) {
        hotelRepo.deleteById(id);
    }

    @Override
    public void deleteAllHotel() {
        hotelRepo.deleteAll();
    }

    @Override
    public Hotel findHotelWithItineraryList(Integer id) {
        return hotelRepo.findHotelWithItineraryList(id);
    }
}
