package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.entity.Drinks;

import java.util.List;

public interface DrinkService {

    Drinks saveDrink(Drinks drink);

    List<Drinks> getAllDrinks();

    Drinks getDrinksById(Long drinkId);

}
