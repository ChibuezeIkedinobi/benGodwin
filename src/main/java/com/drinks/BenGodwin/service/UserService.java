package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Role;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.exception.UserRegistrationException;
import com.drinks.BenGodwin.repository.RoleRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;

    public Users registerUser(Users user) {

            if (usersRepository.findByUsername(user.getUsername()).isPresent()) {
                log.warn("Username already exists: {}", user.getUsername()); // Logging the warning if username already exists
                throw new RuntimeException("Username already exists");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role cashierRole = roleRepository.findByName("CASHIER");
            user.setRoles(Collections.singleton(cashierRole));
            Users savedUser = usersRepository.save(user);
            log.info("User registered: {}", savedUser); // Logging the outcome of user registration
            return savedUser;
    }

    public String loginUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authenticationToken));
        log.info("User logged in: {}", username); // Logging the outcome of user login
        return "User logged in successfully";
    }

    public Users findByUsername(String username) {
        Users user = usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        log.info("User found by username: {}", username); // Logging the outcome of finding user by username
        return user;
    }

    public Optional<Users> getUserById(Long id) {
        Optional<Users> user = usersRepository.findById(id);
        log.info("Fetched user by ID {}: {}", id, user); // Logging the outcome of fetching user by ID
        return user;
    }
}