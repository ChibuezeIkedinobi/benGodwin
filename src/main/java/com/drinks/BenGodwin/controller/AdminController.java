package com.drinks.BenGodwin.controller;

import com.drinks.BenGodwin.dto.*;
import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import com.drinks.BenGodwin.entity.Customer;
import com.drinks.BenGodwin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private AdminService adminService;

    @PostMapping("/brands")
    public ResponseEntity<?> addBrand(@RequestBody BrandDto brandDto) {
        Brand brand = adminService.addBrand(brandDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(brand);
    }

    @PutMapping("/brands/{id}/price")
    public ResponseEntity<?> updateBrandPrice(@PathVariable Long id, @RequestBody BigDecimal newSellingPrice) {
        Brand brand = adminService.updateBrandPrice(id, newSellingPrice);
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/gains/monthly")
    public ResponseEntity<?> getMonthlyGains() {
        List<MonthlyGain> gains = adminService.getMonthlyGains();
        return ResponseEntity.ok(gains);
    }

    @GetMapping("/gains/batch/{batchId}")
    public ResponseEntity<?> getBatchGains(@PathVariable Long batchId) {
        BatchGain batchGain = adminService.getBatchGains(batchId);
        return ResponseEntity.ok(batchGain);
    }

    @GetMapping("/gains/brand/{brandId}/monthly")
    public ResponseEntity<?> getBrandMonthlyGains(@PathVariable Long brandId) {
        List<MonthlyGain> gains = adminService.getBrandMonthlyGains(brandId);
        return ResponseEntity.ok(gains);
    }

    @GetMapping("/batches")
    public ResponseEntity<?> getAllBatches() {
        List<Batch> batches = adminService.getAllBatches();
        return ResponseEntity.ok(batches);
    }

    @PostMapping("/batches")
    public ResponseEntity<?> addBatch(@RequestBody BatchDto batchDto) {
        Batch batch = adminService.addBatch(batchDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(batch);
    }

    @PutMapping("/batches/{id}/complete")
    public ResponseEntity<?> completeBatch(@PathVariable Long id) {
        Batch batch = adminService.completeBatch(id);
        return ResponseEntity.ok(batch);
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = adminService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{customerId}/purchases")
    public ResponseEntity<?> getCustomerPurchases(@PathVariable Long customerId) {
        List<CustomerPurchaseDto> purchases = adminService.getCustomerPurchases(customerId);
        return ResponseEntity.ok(purchases);
    }

    @GetMapping("/customers/{customerId}/monthly-gains")
    public ResponseEntity<?> getCustomerMonthlyGains(@PathVariable Long customerId) {
        List<MonthlyCustomerGainDto> gains = adminService.getCustomerMonthlyGains(customerId);
        return ResponseEntity.ok(gains);
    }

}
