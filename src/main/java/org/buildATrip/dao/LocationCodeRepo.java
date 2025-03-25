package org.buildATrip.dao;

import org.buildATrip.entity.LocationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationCodeRepo extends JpaRepository<LocationCode, String> {
    // Find a location by city name
    LocationCode findByCityNameIgnoreCase(String cityName);
}