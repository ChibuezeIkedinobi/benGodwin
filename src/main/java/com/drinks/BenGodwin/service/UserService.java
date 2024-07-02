package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Role;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.exception.UserRegistrationException;
import com.drinks.BenGodwin.repository.RoleRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final AuthenticationManager authenticationManager;


    public Users registerUser(Users user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            Role cashierRole = roleRepository.findByName("CASHIER");
            user.setRoles(Collections.singleton(cashierRole));
            return usersRepository.save(user);
        } catch (RuntimeException e) {
            throw new UserRegistrationException("Error occurred during user registration", e);
        }
    }

    public String loginUser(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authenticationToken));
        return "User logged in successfully";
    }

    public Users findByUsername(String username) {
        return usersRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    public void saveCashier(Users users) {
        if (usersRepository.findByUsername(users.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setRoles(Collections.singleton(roleRepository.findByName("CASHIER")));
        usersRepository.save(users);
    }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }


}
