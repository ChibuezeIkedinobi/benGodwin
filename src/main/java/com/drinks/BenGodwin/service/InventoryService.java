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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class InventoryService {

    private BatchRepository batchRepository;

    private BrandRepository brandRepository;

    private SaleRepository saleRepository;

    public List<Batch> getLowStockBatches(int threshold) {
        return batchRepository.findByRemainingQuantityLessThan(threshold);
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

    public List<Batch> getAllBatches() {
        return batchRepository.findAll();
    }

    public List<Brand> getAllBrands() {
        return brandRepository.findAll();
    }

    public List<Sale> getSales() {
        return saleRepository.findAll();
    }

    public BigDecimal getTotalGain() {
        List<Sale> sales = saleRepository.findAll();
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

}
