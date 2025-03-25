package org.buildATrip.controller;

import org.buildATrip.entity.Hotel;
import org.buildATrip.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotel")
@CrossOrigin
public class HotelController {

    @Autowired
    private HotelService hotelService;

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable("id") String id) {
        Hotel hotel = hotelService.getHotelById(id);
        return (hotel == null) ? new ResponseEntity<Hotel>(hotel, HttpStatus.NOT_FOUND) : new ResponseEntity<Hotel>(hotel, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("id") String id) {
        hotelService.deleteHotel(id);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

}
