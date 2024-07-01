package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Role;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.repository.RoleRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@AllArgsConstructor
@Service
public class UserService {

    private UsersRepository usersRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;

    @PostConstruct
    public void init() {
        if (roleRepository.findByName("ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("CASHIER") == null) {
            Role cashierRole = new Role();
            cashierRole.setName("CASHIER");
            roleRepository.save(cashierRole);
        }

        if (usersRepository.findByUsername("admin") == null) {
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Collections.singleton(roleRepository.findByName("ADMIN")));
            usersRepository.save(admin);
        }
    }

        public void saveCashier(Users users) {
            if (usersRepository.findByUsername(users.getUsername()) != null) {
                throw new RuntimeException("Username already exists");
            }
            users.setPassword(passwordEncoder.encode(users.getPassword()));
            users.setRoles(Collections.singleton(roleRepository.findByName("CASHIER")));
            usersRepository.save(users);
        }

    public Optional<Users> getUserById(Long id) {
        return usersRepository.findById(id);
    }


//    public boolean validateLogin(String username, String password) {
//        Users users = usersRepository.findByUsername(username);
//        if (users != null) {
//            return passwordEncoder.matches(password, users.getPassword());
//        }
//        return false;
//    }
}
