package org.buildATrip.service;

import org.buildATrip.TestApplicationConfiguration;
import org.buildATrip.dao.UserRepository;
import org.buildATrip.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = TestApplicationConfiguration.class)
class UserServiceImplTest {


    private UserService userService;

    public UserServiceImplTest() {
        UserRepository userRepository = new UserRepositoryStub();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterAndGetUserById() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));

        try {
            userService.registerUser(user1);
            User retrievedUser = userService.getUserById(1);
            assertEquals(user1.getId(), retrievedUser.getId());
            assertEquals(user1.getEmail(), retrievedUser.getEmail());
        } catch (Exception e){
            fail("Should not throw an error.");
        }
    }

    @Test
    public void testLoginWithCorrectPassword() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));
        userService.registerUser(user1);


        User loggedInUser = userService.login(
                "john.doe@example.com", "securepassword");

        assertNotNull(loggedInUser);
        assertEquals("john.doe@example.com", loggedInUser.getEmail());
    }

    @Test
    public void testLoginWithIncorrectPassword() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));
        userService.registerUser(user1);


        User loggedInUser = userService.login(
                "john.doe@example.com", "wrongpass");

        assertNull(loggedInUser);
    }

    @Test
    void testUpdateUserSuccess() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));
        userService.registerUser(user1);

        user1.setEmail("updated.email@example.com");
        User updatedUser = userService.updateUser(user1.getId(), user1);

        assertNotNull(updatedUser);
        assertEquals("updated.email@example.com", updatedUser.getEmail());
    }

    @Test
    void testUpdateUserFailure() {
        User user = new User();
        user.setId(999);

        User updatedUser = userService.updateUser(999, user);

        assertNull(updatedUser);
    }

    @Test
    void testDeleteUser() {
        User user1 = new User();
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setEmail("john.doe@example.com");
        user1.setPassword("securepassword");
        user1.setOriginCity("New York");
        user1.setDateOfBirth(LocalDate.of(1995, 5, 20));
        userService.registerUser(user1);

        userService.deleteUser(user1.getId());

        assertNull(userService.getUserById(user1.getId()));
    }
}