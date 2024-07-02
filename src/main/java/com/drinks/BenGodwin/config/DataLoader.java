package com.drinks.BenGodwin.config;

import com.drinks.BenGodwin.entity.Role;
import com.drinks.BenGodwin.entity.Users;
import com.drinks.BenGodwin.repository.RoleRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;

@Configuration
@RequiredArgsConstructor
public class DataLoader {

    private final RoleRepository roleRepository;
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

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

        if (usersRepository.findByUsername("admin").isEmpty()) {
            Users admin = new Users();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin"));
            admin.setRoles(Collections.singleton(roleRepository.findByName("ADMIN")));
            usersRepository.save(admin);
        }
    }

}
