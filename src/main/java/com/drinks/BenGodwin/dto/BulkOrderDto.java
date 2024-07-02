package com.drinks.BenGodwin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class BulkOrderDto {

    private Long customerId;
    private Long cashierId;
    private List<TransactionItemDto> items;
    private BigDecimal discount; // Field for discount
    private BigDecimal amountPaid; // Field for amount paid

}
