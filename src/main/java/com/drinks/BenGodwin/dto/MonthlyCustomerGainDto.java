package com.drinks.BenGodwin.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class MonthlyCustomerGainDto {

    private int year;
    private int month;
    private BigDecimal totalGain;

}
