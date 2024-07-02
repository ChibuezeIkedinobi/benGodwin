package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.*;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.*;



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
        return brandRepository.save(brand);
    }

    public Brand updateBrandPrice(Long id, BigDecimal newSellingPrice) {
        Brand brand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));
        brand.setSellingPrice(newSellingPrice);
        return brandRepository.save(brand);
    }

    public Batch addBatch(BatchDto batchDto) {
        return getBatch(batchDto, brandRepository, batchRepository);
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
        return new CustomerMonthlySummaryDto(monthlyCustomerGainDto, totalSpent, totalGain);
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

        return new ArrayList<>(monthlyPurchases.values());
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        return transactionRepository.findByCustomerId(customerId);
    }
}