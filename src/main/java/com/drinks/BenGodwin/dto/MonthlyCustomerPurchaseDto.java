package com.drinks.BenGodwin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@AllArgsConstructor
@Getter
@Setter
public class MonthlyCustomerPurchaseDto {

    private int year;
    private String month;
    private Map<String, BrandPurchaseDetails> brandQuantities;
    private BigDecimal totalSpent;

}
