package com.drinks.BenGodwin.controller;

import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Users> registerUser(@RequestBody Users users) {
        Users registeredUsers = userService.registerNewUser(users.getUsername(), users.getPassword());
        if (registeredUsers == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(registeredUsers);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody Users users) {
        if (userService.validateLogin(users.getUsername(), users.getPassword())) {
            return ResponseEntity.ok("Login successful");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

}
