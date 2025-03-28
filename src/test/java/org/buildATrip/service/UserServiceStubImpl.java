package org.buildATrip.service;

import org.buildATrip.entity.Itinerary;
import org.buildATrip.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

public class UserServiceStubImpl implements UserService{
    private final User user1;

    PasswordEncoder passwordEncoder;

    public UserServiceStubImpl(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        user1 = new User();
        user1.setId(1);
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("securepassword"));
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));
    }
    @Override
    public User getUserById(int id) {
        return id == user1.getId() ? user1 : null;
    }

    @Override
    public User getUserByEmail(String email) {
        return user1.getEmail().equals(email) ? user1 : null;
    }

    @Override
    public User registerUser(User user) {
        boolean isInvalid = false;
        if (user.getFirstName().isBlank()){
            user.setFirstName("Name blank, user NOT added");
            isInvalid = true;
        }
        if (user.getLastName().isBlank()){
            user.setLastName("Name blank, user NOT added");
            isInvalid = true;
        }
        if (user.getEmail().isBlank()){
            user.setEmail("Email blank, user NOT added");
            isInvalid = true;
        }
        if (user.getPassword().isBlank()){
            user.setPassword("Password blank, user NOT added");
            isInvalid = true;
        }
        if (user.getOriginCity().isBlank()){
            user.setOriginCity("City blank, user NOT added");
            isInvalid = true;
        }
        if (user.getDateOfBirth() == null){
            user.setLastName("Date blank, user NOT added");
            isInvalid = true;
        }

        if (!isInvalid) {
//            hash the password before saving it
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setId(2);
            return user;
        } else {
            return user;
        }
    }

    @Override
    public User login(String email, String password) {
        return (user1.getEmail().equals(email) && user1.getPassword().equals(password))
                ? user1
                : null;
    }

    @Override
    public User updateUser(int id, User updatedUser) {
        if (id == user1.getId()) {
            user1.setFirstName(updatedUser.getFirstName());
            user1.setLastName(updatedUser.getLastName());
            user1.setOriginCity(updatedUser.getOriginCity());
            return user1;
        }
        return null;
    }

    @Override
    public void deleteUser(int id) {
        //nothing to do
    }

    @Override
    public void deleteAllUser() {
        //nothing to do
    }

    @Override
    public List<Itinerary> getItinerariesForUser(int userId) {
        return List.of();
    }
}