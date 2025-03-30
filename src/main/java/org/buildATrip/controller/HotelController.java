package org.buildATrip.controller;

import org.buildATrip.entity.BoardType;
import org.buildATrip.entity.Hotel;
import org.buildATrip.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/{destination}/{hotelBudget}")
    public ResponseEntity<List<Hotel>> searchHotel(@PathVariable("destination") String destinationCode,
                                                   @PathVariable("hotelBudget") String hotelBudget,
                                                   @RequestParam(required = false) Integer numberAdults,
                                                   @RequestParam(required = false, defaultValue = "#{T(java.time.LocalDate).now()}") LocalDate checkIn,
                                                   @RequestParam(required = false) LocalDate checkOut,
                                                   @RequestParam(required = false) BoardType boardType) {

        List<Hotel> hotelsOffer = hotelService.searchHotel(destinationCode, numberAdults, checkIn, checkOut, hotelBudget, boardType);
        return ResponseEntity.status(HttpStatus.OK).body(hotelsOffer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable("id") Integer id) {
        Hotel hotel = hotelService.getHotelById(id);
        return (hotel == null) ? new ResponseEntity<Hotel>(hotel, HttpStatus.NOT_FOUND) : new ResponseEntity<Hotel>(hotel, HttpStatus.OK);
    }

    @PostMapping()
    public ResponseEntity<Hotel> createNewHotel(@RequestBody Hotel hotel) {
        Hotel createdHotel = hotelService.createHotel(hotel);
        return new ResponseEntity<>(createdHotel, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("id") Integer id) {
        hotelService.deleteHotel(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
