package org.buildATrip.dao;

import org.buildATrip.entity.Flight;
import org.buildATrip.entity.LocationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FlightRepo  extends JpaRepository<Flight, Integer> {


    // Find flights by origin and destination
    List<Flight> findByOriginCodeAndDestinationCode(LocationCode originCode, LocationCode destinationCode);

    // Find flights by origin, destination and date
    List<Flight> findByOriginCodeAndDestinationCodeAndDate(
            LocationCode originCode,
            LocationCode destinationCode,
            LocalDate date);
}
