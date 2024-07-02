package com.drinks.BenGodwin.config;

import com.drinks.BenGodwin.entity.Role;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.repository.RoleRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    public void init() {
        log.info("Initializing data loader..."); // Logging the start of the data loader initialization

        if (roleRepository.findByName("ADMIN") == null) {
            log.info("Creating ADMIN role"); // Logging the creation of the ADMIN role
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepository.save(adminRole);
        }

        if (roleRepository.findByName("CASHIER") == null) {
            log.info("Creating CASHIER role"); // Logging the creation of the CASHIER role
            Role cashierRole = new Role();
            cashierRole.setName("CASHIER");
            roleRepository.save(cashierRole);
        }

        if (usersRepository.findByUsername("admin").isEmpty()) {
            log.info("Creating admin user"); // Logging the creation of the admin user
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Collections.singleton(roleRepository.findByName("ADMIN")));
            usersRepository.save(admin);
        }

        log.info("Data loader initialization completed."); // Logging the completion of the data loader initialization
    }

}
