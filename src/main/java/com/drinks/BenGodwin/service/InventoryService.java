package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.dto.BatchDto;
import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import com.drinks.BenGodwin.entity.Sale;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.BatchRepository;
import com.drinks.BenGodwin.repository.BrandRepository;
import com.drinks.BenGodwin.repository.SaleRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;


@Slf4j
@RequiredArgsConstructor
@Service
public class InventoryService {

    private final BatchRepository batchRepository;
    private final BrandRepository brandRepository;
    private final SaleRepository saleRepository;

    public List<Batch> getLowStockBatches(int threshold) {
        List<Batch> lowStockBatches = batchRepository.findByRemainingQuantityLessThan(threshold);
        log.info("Low stock batches fetched with threshold {}: {}", threshold, lowStockBatches); // Logging the outcome of fetching low stock batches
        return lowStockBatches;
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

    public List<Batch> getAllBatches() {
        List<Batch> batches = batchRepository.findAll();
        log.info("All batches fetched: {}", batches); // Logging the outcome of fetching all batches
        return batches;
    }

    public List<Brand> getAllBrands() {
        List<Brand> brands = brandRepository.findAll();
        log.info("All brands fetched: {}", brands); // Logging the outcome of fetching all brands
        return brands;
    }

    public List<Sale> getSales() {
        List<Sale> sales = saleRepository.findAll();
        log.info("All sales fetched: {}", sales); // Logging the outcome of fetching all sales
        return sales;
    }

    public BigDecimal getTotalGain() {
        List<Sale> sales = saleRepository.findAll();
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
}
