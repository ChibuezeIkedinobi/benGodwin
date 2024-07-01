package com.drinks.BenGodwin.controller;

import com.drinks.BenGodwin.dto.UsersDTO;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;


    @PostMapping("/register/cashier")
    public String registerCashier(@RequestBody Users users) {
        try {
            userService.saveCashier(users);
            return "Cashier registered successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Users user) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return ResponseEntity.ok("User logged in: " + userDetails.getUsername());
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

//    @PostMapping("/login")
//    public ResponseEntity<String> loginUser(@RequestBody Users users) {
//        if (userService.validateLogin(users.getUsername(), users.getPassword())) {
//            return ResponseEntity.ok("Login successful");
//        }
//        return ResponseEntity.status(401).body("Invalid credentials");
//    }

}
