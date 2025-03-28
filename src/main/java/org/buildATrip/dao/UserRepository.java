package org.buildATrip.dao;

import org.buildATrip.entity.Itinerary;
import org.buildATrip.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    @Query("SELECT u.itineraryList FROM User u WHERE u.id = ?1")
    List<Itinerary> findItinerariesByUserId(int userId);

}
