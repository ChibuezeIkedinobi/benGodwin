package com.drinks.BenGodwin.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CustomerMonthlySummaryDto {

    private List<MonthlyCustomerGainDto> monthlyData;
    private BigDecimal totalSpent;
    private BigDecimal totalGain;

}
