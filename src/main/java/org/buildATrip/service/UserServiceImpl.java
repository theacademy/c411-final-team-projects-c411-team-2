package org.buildATrip.service;

import org.buildATrip.dao.UserRepository;
import org.buildATrip.entity.Itinerary;
import org.buildATrip.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;


    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public User getUserByEmail(String email){
        return userRepository.findByEmail(email).orElse(null);
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
            return userRepository.save(user);
        } else {
            return user;
        }

    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.findByEmail(email).orElse(null);
        if (user != null) {
            if (passwordEncoder.matches(password, user.getPassword())) {
                return user;
            }
        }
        return null;
    }

    @Override
    public User updateUser(int id, User user) {
        if(id == user.getId() && userRepository.existsById(id)) {
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public void deleteAllUser() {
        userRepository.deleteAll();
    }

    @Override
    public List<Itinerary> getItinerariesForUser(int userId) {
        return userRepository.findItinerariesByUserId(userId);
    }
}
