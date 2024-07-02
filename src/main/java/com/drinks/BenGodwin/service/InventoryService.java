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
        return getBatch(batchDto, brandRepository, batchRepository);
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
        return getBigDecimal(sales);
    }

}
