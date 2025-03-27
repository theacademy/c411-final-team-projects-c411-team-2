package org.buildATrip.dao;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
    }

    public void testSaveGetUser() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("securepassword")); // Encode before saving
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User savedUser = userRepository.save(user1);


        assertNotNull(savedUser.getId());

        Optional<User> retrievedUser = userRepository.findByEmail("john.doe@example.com");
        assertTrue(retrievedUser.isPresent());

        User actualUser = retrievedUser.get();
        assertEquals("john.doe@example.com", actualUser.getEmail());

    }

    @Test
    public void testFindById() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("securepassword")); // Encode before saving
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User savedUser = userRepository.save(user1);

        Optional<User> foundUser = userRepository.findById(user1.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(user1.getEmail(), foundUser.get().getEmail());
    }

    @Test
    public void testFindByIdNotFound() {
        Optional<User> foundUser = userRepository.findById(999);
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testFindAll() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("securepassword")); // Encode before saving
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user2 = new User();
        user2.setFirstName("Johny");
        user2.setLastName("Doe");
        user2.setEmail("john.doe@exammmple.com");
        user2.setPassword(passwordEncoder.encode("securepassword")); // Encode before saving
        user2.setOriginCity("New York");
        user2.setDateOfBirth(LocalDate.of(1995, 5, 20));

        userRepository.saveAll(List.of(user1, user2));

        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());
    }


    @Test
    public void testFindByEmail() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User savedUser = userRepository.save(user1);

        Optional<User> foundUser = userRepository.findByEmail("john.doe@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("john.doe@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testFindByEmailNotFound() {
        Optional<User> foundUser = userRepository.findByEmail("nonexistent@example.com");
        assertFalse(foundUser.isPresent());
    }


    @Test
    public void testDeleteUserById() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User savedUser = userRepository.save(user1);

        userRepository.deleteById(savedUser.getId());

        Optional<User> deletedUser = userRepository.findById(savedUser.getId());
        assertFalse(deletedUser.isPresent());
    }

    @Test
    public void testDeleteAll() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword(passwordEncoder.encode("securepassword")); // Encode before saving
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        User user2 = new User();
        user2.setFirstName("Johny");
        user2.setLastName("Doe");
        user2.setEmail("john.doe@exammmple.com");
        user2.setPassword(passwordEncoder.encode("securepassword")); // Encode before saving
        user2.setOriginCity("New York");
        user2.setDateOfBirth(LocalDate.of(1995, 5, 20));

        userRepository.saveAll(List.of(user1, user2));
        userRepository.deleteAll();

        List<User> users = userRepository.findAll();
        assertTrue(users.isEmpty());
    }




}