package com.drinks.BenGodwin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class TransactionItemDto {

    private Long brandId;
    private int quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;

}
