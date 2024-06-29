package com.drinks.BenGodwin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class BrandDto {

    private String name;
    private BigDecimal buyingPrice;
    private BigDecimal sellingPrice;

}
