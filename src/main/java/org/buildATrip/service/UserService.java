package org.buildATrip.service;


import org.buildATrip.entity.Itinerary;
import org.buildATrip.entity.User;

import java.util.List;

public interface UserService {
    User getUserById(int id);
    User getUserByEmail(String email);
    User registerUser(User user);
    User login(String email, String password);
    User updateUser(int id, User user);
    void deleteUser(int id);
    void deleteAllUser(); // used to clean up test db
    List<Itinerary> getItinerariesForUser(int userId);
}
