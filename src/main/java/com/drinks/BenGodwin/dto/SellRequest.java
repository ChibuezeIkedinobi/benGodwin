package com.drinks.BenGodwin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class SellRequest {

    private Long bookId;
    private Long buyerId;
    private int quantitySold;
    private BigDecimal sellingPrice;

}
