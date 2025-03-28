package org.buildATrip.dao;

import org.buildATrip.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface HotelRepo extends JpaRepository<Hotel, Integer> {

    @Query("SELECT h FROM Hotel h JOIN FETCH h.itineraryList WHERE h.hotel_id = :hotelId")
    Hotel findHotelWithItineraryList(@Param("hotelId") Integer hotelId);
    
}
