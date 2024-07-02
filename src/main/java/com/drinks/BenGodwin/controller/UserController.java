package com.drinks.BenGodwin.controller;

import com.drinks.BenGodwin.dto.UsersDTO;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;


    @PostMapping("/register/cashier")
    public ResponseEntity<?> registerUser(@RequestBody Users user) {
        Users registeredUser = userService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        try {
                String message = userService.loginUser(user.getUsername(), user.getPassword());
            return ResponseEntity.ok(message);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<Users> userOptional = userService.getUserById(id);
        if (userOptional.isPresent()) {
            Users user = userOptional.get();
            UsersDTO userDTO = new UsersDTO(user.getId(), user.getUsername(), user.getRoles());
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

}
