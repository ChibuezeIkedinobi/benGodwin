package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findById(Long cashierId);

    Optional<Users> findByUsername(String username);

//    boolean existsByRoles(String role);
}
