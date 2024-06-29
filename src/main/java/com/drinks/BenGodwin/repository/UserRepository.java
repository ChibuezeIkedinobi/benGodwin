package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findById(Long cashierId);

    User findByUsername(String username);
}
