package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Drinks;
import com.drinks.BenGodwin.repository.DrinkRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@AllArgsConstructor
@Service
public class DrinkServiceImpl implements DrinkService {

    DrinkRepository drinkRepository;

    @Override
    public Drinks saveDrink(Drinks drink) {
        try {
            return drinkRepository.save(drink);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save drink: " + e.getMessage());
        }
    }

    @Override
    public List<Drinks> getAllDrinks() {
        try {
            return drinkRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve drink: " + e.getMessage());
        }
    }

    @Override
    public Drinks getDrinksById(Long drinkId) {
        return drinkRepository.findById(drinkId)
                .orElseThrow(() -> new RuntimeException("Drink not found with ID: " + drinkId));
    }
}
