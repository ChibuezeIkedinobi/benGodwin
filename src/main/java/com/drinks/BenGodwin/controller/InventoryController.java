package com.drinks.BenGodwin.controller;


import com.drinks.BenGodwin.dto.BatchDto;
import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import com.drinks.BenGodwin.entity.Sale;
import com.drinks.BenGodwin.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/low-stock")
    public ResponseEntity<?> getLowStockBatches(@RequestParam int threshold) {
        List<Batch> lowStockBatches = inventoryService.getLowStockBatches(threshold);
        return ResponseEntity.ok(lowStockBatches);
    }

    @PostMapping("/batches")
    public ResponseEntity<?> addBatch(@RequestBody BatchDto batchDto) {
        Batch batch = inventoryService.addBatch(batchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(batch);
    }

    @GetMapping("/batches")
    public ResponseEntity<?> getAllBatches() {
        List<Batch> batches = inventoryService.getAllBatches();
        return ResponseEntity.ok(batches);
    }

    @GetMapping("/brands")
    public ResponseEntity<?> getAllBrands() {
        List<Brand> brands = inventoryService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/sales")
    public ResponseEntity<?> getSales() {
        List<Sale> sales = inventoryService.getSales();
        return ResponseEntity.ok(sales);
    }

    @GetMapping("/total-gain")
    public ResponseEntity<?> getTotalGain() {
        BigDecimal totalGain = inventoryService.getTotalGain();
        return ResponseEntity.ok(totalGain);
    }

}
