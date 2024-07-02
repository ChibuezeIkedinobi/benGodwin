package com.drinks.BenGodwin.controller;


import com.drinks.BenGodwin.dto.BatchDto;
import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import com.drinks.BenGodwin.entity.Sale;
import com.drinks.BenGodwin.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockBatches(@RequestParam int threshold) {
        List<Batch> lowStockBatches = inventoryService.getLowStockBatches(threshold);
        log.info("Fetched low stock batches with threshold {}: {}", threshold, lowStockBatches); // Logging the outcome of fetching low stock batches
        return ResponseEntity.ok(lowStockBatches);
    }

    @PostMapping("/batches")
    public ResponseEntity<?> addBatch(@RequestBody BatchDto batchDto) {
        Batch batch = inventoryService.addBatch(batchDto);
        log.info("Batch added: {}", batch); // Logging the outcome of adding a batch
        return ResponseEntity.status(HttpStatus.CREATED).body(batch);
    }

    @GetMapping("/batches")
    public ResponseEntity<?> getAllBatches() {
        List<Batch> batches = inventoryService.getAllBatches();
        log.info("Fetched all batches: {}", batches); // Logging the outcome of fetching all batches
        return ResponseEntity.ok(batches);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        List<Brand> brands = inventoryService.getAllBrands();
        log.info("Fetched all brands: {}", brands); // Logging the outcome of fetching all brands
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getSales() {
        List<Sale> sales = inventoryService.getSales();
        log.info("Fetched all sales: {}", sales); // Logging the outcome of fetching all sales
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/total-gain")
    public ResponseEntity<?> getTotalGain() {
        BigDecimal totalGain = inventoryService.getTotalGain();
        log.info("Fetched total gain: {}", totalGain); // Logging the outcome of fetching total gain
        return ResponseEntity.ok(totalGain);
    }

}