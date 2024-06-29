package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.*;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class AdminService {

    private BrandRepository brandRepository;

    private BatchRepository batchRepository;

    private SaleRepository saleRepository;

    private TransactionRepository transactionRepository;

    private CustomerRepository customerRepository;

    private GainService gainService;

    public Brand addBrand(BrandDto brandDto) {
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brand.setBuyingPrice(brandDto.getBuyingPrice());
        brand.setSellingPrice(brandDto.getSellingPrice());
        return brandRepository.save(brand);
    }

    public Brand updateBrandPrice(Long id, BigDecimal newSellingPrice) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        brand.setSellingPrice(newSellingPrice);
        return brandRepository.save(brand);
    }

    public Batch addBatch(BatchDto batchDto) {
        Batch batch = new Batch();
        batch.setBrand(brandRepository.findById(batchDto.getBrandId())
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found")));
        batch.setQuantity(batchDto.getQuantity());
        batch.setRemainingQuantity(batchDto.getQuantity());
        batch.setBatchPrice(batchDto.getBatchPrice());
        batch.setCreatedAt(LocalDateTime.now());
        batch.setCompleted(false);
        return batchRepository.save(batch);
    }

    public Batch completeBatch(Long id) {
        Batch batch = batchRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
        batch.setCompleted(true);
        return batchRepository.save(batch);
    }

    public List<MonthlyGain> getMonthlyGains() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Sale> sales = saleRepository.findByMonth(startOfMonth, endOfMonth); // Fetch sales using SaleRepository
        BigDecimal totalGain = gainService.calculateTotalGain(sales);
        MonthlyGain monthlyGain = new MonthlyGain(startOfMonth.getYear(), startOfMonth.getMonthValue(), totalGain);
        return Collections.singletonList(monthlyGain);
    }

    public BatchGain getBatchGains(Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
        List<Sale> sales = saleRepository.findByBatch(batch); // Fetch sales using SaleRepository
        BigDecimal totalGain = gainService.calculateTotalGain(sales);
        return new BatchGain(batch, totalGain);
    }

    public List<MonthlyGain> getBrandMonthlyGains(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Sale> sales = saleRepository.findByBrandAndMonth(brand, startOfMonth, endOfMonth); // Fetch sales using SaleRepository
        BigDecimal totalGain = gainService.calculateTotalGain(sales);
        MonthlyGain monthlyGain = new MonthlyGain(startOfMonth.getYear(), startOfMonth.getMonthValue(), totalGain);
        return Collections.singletonList(monthlyGain);
    }

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public List<CustomerPurchaseDto> getCustomerPurchases(Long customerId) {
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);
        List<CustomerPurchaseDto> purchases = new ArrayList<>();

        for (Transaction transaction : transactions) {
            BigDecimal transactionGain = gainService.calculateTransactionGain(transaction);
            purchases.add(new CustomerPurchaseDto(transaction.getId(), transaction.getCreatedAt(), transaction.getTotalAmount(), transactionGain));
        }

        return purchases;
    }

    public List<MonthlyCustomerGainDto> getCustomerMonthlyGains(Long customerId) {
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);
        Map<String, BigDecimal> monthlyGains = new HashMap<>();

        for (Transaction transaction : transactions) {
            String monthKey = transaction.getCreatedAt().getYear() + "-" + transaction.getCreatedAt().getMonthValue();
            BigDecimal transactionGain = gainService.calculateTransactionGain(transaction);
            monthlyGains.put(monthKey, monthlyGains.getOrDefault(monthKey, BigDecimal.ZERO).add(transactionGain));
        }

        return getMonthlyCustomerGainDtos(monthlyGains);
    }

    @NotNull
    static List<MonthlyCustomerGainDto> getMonthlyCustomerGainDtos(Map<String, BigDecimal> monthlyGains) {
        return getMonthlyCustomerGainDtos(monthlyGains);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    // Method definition for fetching transactions by customer ID
    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }

}
