package com.drinks.BenGodwin.repository;

import com.drinks.BenGodwin.entity.Drinks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DrinkRepository extends JpaRepository<Drinks, Long> {
}
