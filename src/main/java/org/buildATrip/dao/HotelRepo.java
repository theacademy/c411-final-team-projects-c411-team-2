package org.buildATrip.dao;

import org.buildATrip.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepo extends JpaRepository<Hotel, String> {

    
}
