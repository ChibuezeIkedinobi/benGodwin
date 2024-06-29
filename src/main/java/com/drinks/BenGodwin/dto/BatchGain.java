package com.drinks.BenGodwin.dto;

import com.drinks.BenGodwin.entity.Batch;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
@Setter
public class BatchGain {

    private Batch batch;
    private BigDecimal totalGain;

}
