package org.buildATrip.dao;

import org.buildATrip.entity.Itinerary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItineraryRepo extends JpaRepository<Itinerary, Integer> {

}
