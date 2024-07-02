package com.drinks.BenGodwin.controller;

import com.drinks.BenGodwin.dto.UsersDTO;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register/cashier")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        Users registeredUser = userService.registerUser(user);
        log.info("User registered: {}", registeredUser); // Logging the outcome of user registration
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        try {
            String message = userService.loginUser(user.getUsername(), user.getPassword());
            log.info("User logged in: {}", user.getUsername()); // Logging the outcome of user login
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            log.warn("Invalid login attempt for user: {}", user.getUsername()); // Logging invalid login attempt
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<Users> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            UsersDTO userDTO = new UsersDTO(user.getId(), user.getUsername(), user.getRoles());
            log.info("Fetched user by ID {}: {}", id, userDTO); // Logging the outcome of fetching user by ID
            return ResponseEntity.ok(userDTO);
        } else {
            log.warn("User not found with ID: {}", id); // Logging user not found
            return ResponseEntity.status(404).body("User not found");
        }
    }

}