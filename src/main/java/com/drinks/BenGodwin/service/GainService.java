package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.CustomerPurchaseDto;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.repository.SaleRepository;
import com.drinks.BenGodwin.repository.TransactionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class GainService {


    private final SaleRepository saleRepository;

    private final TransactionRepository transactionRepository;



    BigDecimal calculateTotalGain(List<Sale> sales) {
        return getBigDecimal(sales);
    }

    @NotNull
    static BigDecimal getBigDecimal(List<Sale> sales) {
        BigDecimal totalGain = BigDecimal.ZERO;

        for (Sale sale : sales) {
            Batch batch = sale.getBatch();
            BigDecimal costPrice = batch.getBatchPrice().divide(new BigDecimal(batch.getQuantity()), RoundingMode.HALF_UP);
            BigDecimal gain = sale.getSellingPrice().subtract(costPrice).multiply(new BigDecimal(sale.getQuantitySold()));
            totalGain = totalGain.add(gain);
        }

        return totalGain;
    }

    public BigDecimal calculateTransactionGain(Transaction transaction) {
        BigDecimal transactionGain = BigDecimal.ZERO;
        for (TransactionItem item : transaction.getItems()) {
            Batch batch = item.getBatch();
            BigDecimal costPrice = BigDecimal.ZERO;
            if (batch.getQuantity() != 0) {
                costPrice = batch.getBatchPrice().divide(new BigDecimal(batch.getQuantity()), RoundingMode.HALF_UP);
            }
            BigDecimal gain = item.getUnitPrice().subtract(costPrice).multiply(new BigDecimal(item.getQuantity()));
            transactionGain = transactionGain.add(gain);
        }
        return transactionGain;
    }
}
