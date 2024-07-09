package com.Bontuyan.AddData.controller;

import com.Bontuyan.AddData.model.User;
import com.Bontuyan.AddData.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api")
public class UserController {

    private static final Logger logger = Logger.getLogger(UserController.class.getName());

    @Autowired
    private UserRepository userRepository;

    // Fetch all users
    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // Fetch a user by ID
    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUserById(@PathVariable String id) {
        logger.info("Fetching user with id: " + id);
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }

    // Signup a new user
    @PostMapping("/users")
    public ResponseEntity<Map<String, String>> signup(@RequestBody User user) {
        Map<String, String> response = new HashMap<>();

        // Check for null parameters
        if (user.getUsername() == null || user.getPassword() == null || user.getJob() == null || user.getJobDescription() == null) {
            response.put("message", "Username, Password, Job, or Job Description is null");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        // Check if user already exists
        if (userRepository.findByUsername(user.getUsername()) != null) {
            response.put("message", "User already exists");
            return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        } else {
            // Save user to MongoDB
            userRepository.save(user);

            // Successful signup
            response.put("message", "Signup Successfully");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
}
