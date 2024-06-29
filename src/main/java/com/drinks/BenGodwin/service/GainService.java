package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.CustomerPurchaseDto;
import com.drinks.BenGodwin.dto.MonthlyCustomerGainDto;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.repository.SaleRepository;
import com.drinks.BenGodwin.repository.TransactionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.drinks.BenGodwin.service.AdminService.getMonthlyCustomerGainDtos;

@AllArgsConstructor
@Service
public class GainService {


    private SaleRepository saleRepository;

    private TransactionRepository transactionRepository;

    public BigDecimal calculateBatchGain(Batch batch) {
        List<Sale> sales = saleRepository.findByBatch(batch);
        return calculateTotalGain(sales);
    }

    public BigDecimal calculateMonthlyGain(LocalDate startOfMonth, LocalDate endOfMonth) {
        List<Sale> sales = saleRepository.findByMonth(startOfMonth, endOfMonth);
        return calculateTotalGain(sales);
    }

    public BigDecimal calculateBrandMonthlyGain(Brand brand, LocalDate startOfMonth, LocalDate endOfMonth) {
        List<Sale> sales = saleRepository.findByBrandAndMonth(brand, startOfMonth, endOfMonth);
        return calculateTotalGain(sales);
    }

    public List<CustomerPurchaseDto> getCustomerPurchases(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        List<CustomerPurchaseDto> purchases = new ArrayList<>();

        for (Transaction transaction : transactions) {
            BigDecimal transactionGain = calculateTransactionGain(transaction);
            purchases.add(new CustomerPurchaseDto(transaction.getId(), transaction.getCreatedAt(), transaction.getTotalAmount(), transactionGain));
        }

        return purchases;
    }

    public List<MonthlyCustomerGainDto> getCustomerMonthlyGains(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        Map<String, BigDecimal> monthlyGains = new HashMap<>();

        for (Transaction transaction : transactions) {
            String monthKey = transaction.getCreatedAt().getYear() + "-" + transaction.getCreatedAt().getMonthValue();
            BigDecimal transactionGain = calculateTransactionGain(transaction);
            monthlyGains.put(monthKey, monthlyGains.getOrDefault(monthKey, BigDecimal.ZERO).add(transactionGain));
        }

        return getMonthlyCustomerGainDtos(monthlyGains);
    }

    @NotNull
    static List<MonthlyCustomerGainDto> getMonthlyCustomerGainDtos(Map<String, BigDecimal> monthlyGains) {
        List<MonthlyCustomerGainDto> monthlyGainsList = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : monthlyGains.entrySet()) {
            String[] parts = entry.getKey().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            monthlyGainsList.add(new MonthlyCustomerGainDto(year, month, entry.getValue()));
        }

        return monthlyGainsList;
    }

    BigDecimal calculateTotalGain(List<Sale> sales) {
        BigDecimal totalGain = BigDecimal.ZERO;

        for (Sale sale : sales) {
            Batch batch = sale.getBatch();
            BigDecimal costPrice = batch.getBatchPrice().divide(new BigDecimal(batch.getQuantity()), RoundingMode.HALF_UP);
            BigDecimal gain = sale.getSellingPrice().subtract(costPrice).multiply(new BigDecimal(sale.getQuantitySold()));
            totalGain = totalGain.add(gain);
        }

        return totalGain;
    }

    BigDecimal calculateTransactionGain(Transaction transaction) {
        BigDecimal transactionGain = BigDecimal.ZERO;
        for (TransactionItem item : transaction.getItems()) {
            Batch batch = item.getBatch();
            BigDecimal costPrice = batch.getBatchPrice().divide(new BigDecimal(batch.getQuantity()), RoundingMode.HALF_UP);
            BigDecimal gain = item.getPrice().subtract(costPrice).multiply(new BigDecimal(item.getQuantity()));
            transactionGain = transactionGain.add(gain);
        }
        return transactionGain;
    }
}
