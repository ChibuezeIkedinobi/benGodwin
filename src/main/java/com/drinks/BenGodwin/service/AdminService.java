package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.*;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;



@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AdminService {

    private final BrandRepository brandRepository;
    private final BatchRepository batchRepository;
    private final SaleRepository saleRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final GainService gainService;

    public Brand addBrand(BrandDto brandDto) {
        Brand brand = new Brand();
        brand.setName(brandDto.getName());
        brand.setBuyingPrice(brandDto.getBuyingPrice());
        brand.setSellingPrice(brandDto.getSellingPrice());
        Brand savedBrand = brandRepository.save(brand);
        log.info("Brand added: {}", savedBrand); // Logging the outcome of adding a brand
        return savedBrand;
    }

    public Brand updateBrandPrice(Long id, BigDecimal newSellingPrice) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        brand.setSellingPrice(newSellingPrice);
        Brand updatedBrand = brandRepository.save(brand);
        log.info("Updated brand price: {}", updatedBrand); // Logging the outcome of updating brand price
        return updatedBrand;
    }

    public Batch addBatch(BatchDto batchDto) {
        Batch batch = getBatch(batchDto, brandRepository, batchRepository);
        log.info("Batch added: {}", batch); // Logging the outcome of adding a batch
        return batch;
    }

    @NotNull
    static Batch getBatch(BatchDto batchDto, BrandRepository brandRepository, BatchRepository batchRepository) {
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
        Batch completedBatch = batchRepository.save(batch);
        log.info("Batch completed: {}", completedBatch); // Logging the outcome of completing a batch
        return completedBatch;
    }

    public List<MonthlyGain> getMonthlyGains() {
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Sale> sales = saleRepository.findByMonth(startOfMonth, endOfMonth); // Fetch sales using SaleRepository
        BigDecimal totalGain = gainService.calculateTotalGain(sales);
        MonthlyGain monthlyGain = new MonthlyGain(startOfMonth.getYear(), startOfMonth.getMonthValue(), totalGain);
        log.info("Monthly gains calculated: {}", monthlyGain); // Logging the outcome of calculating monthly gains
        return Collections.singletonList(monthlyGain);
    }

    public BatchGain getBatchGains(Long batchId) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));
        List<Sale> sales = saleRepository.findByBatch(batch); // Fetch sales using SaleRepository
        BigDecimal totalGain = gainService.calculateTotalGain(sales);
        BatchGain batchGain = new BatchGain(batch, totalGain);
        log.info("Batch gains calculated: {}", batchGain); // Logging the outcome of calculating batch gains
        return batchGain;
    }

    public List<MonthlyGain> getBrandMonthlyGains(Long brandId) {
        Brand brand = brandRepository.findById(brandId)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        LocalDate startOfMonth = LocalDate.now().withDayOfMonth(1);
        LocalDate endOfMonth = startOfMonth.plusMonths(1).minusDays(1);
        List<Sale> sales = saleRepository.findByBrandAndMonth(brand, startOfMonth, endOfMonth); // Fetch sales using SaleRepository
        BigDecimal totalGain = gainService.calculateTotalGain(sales);
        MonthlyGain monthlyGain = new MonthlyGain(startOfMonth.getYear(), startOfMonth.getMonthValue(), totalGain);
        log.info("Brand monthly gains calculated: {}", monthlyGain); // Logging the outcome of calculating brand monthly gains
        return Collections.singletonList(monthlyGain);
    }

    public List<Batch> getAllBatches() {
        List<Batch> batches = batchRepository.findAll();
        log.info("All batches fetched: {}", batches); // Logging the outcome of fetching all batches
        return batches;
    }

    public CustomerMonthlySummaryDto getCustomerMonthlyGains(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        Map<String, MonthlyCustomerGainDto> monthlyGains = new HashMap<>();

        BigDecimal totalSpent = BigDecimal.ZERO;
        BigDecimal totalGain = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            String monthKey = transaction.getCreatedAt().getYear() + "-" + transaction.getCreatedAt().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            BigDecimal transactionGain = gainService.calculateTransactionGain(transaction);
            totalSpent = totalSpent.add(transaction.getTotalAmount());
            totalGain = totalGain.add(transactionGain);

            MonthlyCustomerGainDto monthlyDto = monthlyGains.getOrDefault(monthKey, new MonthlyCustomerGainDto(transaction.getCreatedAt().getYear(), transaction.getCreatedAt().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), BigDecimal.ZERO, BigDecimal.ZERO));
            monthlyDto.setTotalSpent(monthlyDto.getTotalSpent().add(transaction.getTotalAmount()));
            monthlyDto.setTotalGain(monthlyDto.getTotalGain().add(transactionGain));
            monthlyGains.put(monthKey, monthlyDto);
        }

        List<MonthlyCustomerGainDto> monthlyCustomerGainDto = new ArrayList<>(monthlyGains.values());
        CustomerMonthlySummaryDto summaryDto = new CustomerMonthlySummaryDto(monthlyCustomerGainDto, totalSpent, totalGain);
        log.info("Customer monthly gains calculated for customer ID {}: {}", customerId, summaryDto); // Logging the outcome of calculating customer monthly gains
        return summaryDto;
    }

    public List<MonthlyCustomerPurchaseDto> getCustomerMonthlyPurchases(Long customerId) {
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);
        Map<String, MonthlyCustomerPurchaseDto> monthlyPurchases = new HashMap<>();
        BigDecimal grandTotalSpent = BigDecimal.ZERO;

        for (Transaction transaction : transactions) {
            String monthKey = transaction.getCreatedAt().getYear() + "-" + transaction.getCreatedAt().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            MonthlyCustomerPurchaseDto monthlyDto = monthlyPurchases.getOrDefault(monthKey, new MonthlyCustomerPurchaseDto(transaction.getCreatedAt().getYear(), transaction.getCreatedAt().getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH), new HashMap<>(), BigDecimal.ZERO));

            for (TransactionItem item : transaction.getItems()) {
                String brandName = item.getBrand().getName();
                BrandPurchaseDetails details = monthlyDto.getBrandQuantities().getOrDefault(brandName, new BrandPurchaseDetails(0, item.getUnitPrice(), BigDecimal.ZERO));
                details.setQuantity(details.getQuantity() + item.getQuantity());
                details.setTotalPrice(details.getTotalPrice().add(item.getTotalPrice()));

                monthlyDto.getBrandQuantities().put(brandName, details);
            }

            monthlyDto.setTotalSpent(monthlyDto.getTotalSpent().add(transaction.getTotalAmount()));
            grandTotalSpent = grandTotalSpent.add(transaction.getTotalAmount());
            monthlyPurchases.put(monthKey, monthlyDto);
        }

        List<MonthlyCustomerPurchaseDto> purchaseDtoList = new ArrayList<>(monthlyPurchases.values());
        log.info("Customer monthly purchases calculated for customer ID {}: {}", customerId, purchaseDtoList); // Logging the outcome of calculating customer monthly purchases
        return purchaseDtoList;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        log.info("All customers fetched: {}", customers); // Logging the outcome of fetching all customers
        return customers;
    }

    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        List<Transaction> transactions = transactionRepository.findByCustomerId(customerId);
        log.info("Fetched transactions for customer ID {}: {}", customerId, transactions); // Logging the outcome of fetching transactions by customer ID
        return transactions;
    }
}