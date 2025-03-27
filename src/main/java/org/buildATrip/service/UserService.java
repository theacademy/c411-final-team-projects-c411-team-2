package org.buildATrip.service;


import org.buildATrip.entity.User;

public interface UserService {
    User getUserById(int id);
    User getUserByEmail(String email);
    User registerUser(User user);
    User login(String email, String password);
    User updateUser(int id, User user);
    void deleteUser(int id);
//    List<Itinerary> getItinerariesForUser(int userId);
}
