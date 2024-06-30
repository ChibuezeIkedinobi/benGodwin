package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.repository.RoleRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    public Users registerNewUser(String username, String password) {
        if (usersRepository.findByUsername(username).isPresent()) {
            throw new IllegalStateException("Username already taken");
        }

        Users newUsers = Users.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        return usersRepository.save(newUsers);
    }


    public boolean validateLogin(String username, String password) {
        Optional<Users> optionalUsers = usersRepository.findByUsername(username);
        if (optionalUsers.isPresent()) {
            Users users = optionalUsers.get();
            return passwordEncoder.matches(password, users.getPassword());
        }
        return false;
    }
}
