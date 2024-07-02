package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.entity.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class GainService {

    BigDecimal calculateTotalGain(List<Sale> sales) {
        BigDecimal totalGain = getBigDecimal(sales);
        log.info("Total gain calculated: {}", totalGain); // Logging the outcome of calculating total gain
        return totalGain;
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
        log.info("Transaction gain calculated: {}", transactionGain); // Logging the outcome of calculating transaction gain
        return transactionGain;
    }
}