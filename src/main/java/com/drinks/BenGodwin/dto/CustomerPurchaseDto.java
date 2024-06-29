package com.drinks.BenGodwin.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
public class CustomerPurchaseDto {

    private Long transactionId;
    private LocalDateTime transactionDate;
    private BigDecimal totalAmount;
    private BigDecimal gain;

}
