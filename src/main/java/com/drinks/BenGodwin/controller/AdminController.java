package com.drinks.BenGodwin.controller;

import com.drinks.BenGodwin.dto.*;
import com.drinks.BenGodwin.entity.Batch;
import com.drinks.BenGodwin.entity.Brand;
import com.drinks.BenGodwin.entity.Customer;
import com.drinks.BenGodwin.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/brands")
    public ResponseEntity<?> addBrand(@RequestBody BrandDto brandDto) {
        log.info("Adding a new brand: {}", brandDto); // Logging the request to add a new brand
        Brand brand = adminService.addBrand(brandDto);
        log.info("Brand added: {}", brand); // Logging the successful addition of the brand
        return ResponseEntity.status(HttpStatus.CREATED).body(brand);
    }

    @PutMapping("/brands/{id}/price")
    public ResponseEntity<?> updateBrandPrice(@PathVariable Long id, @RequestBody BigDecimal newSellingPrice) {
        log.info("Updating price for brand ID {}: {}", id, newSellingPrice); // Logging the request to update brand price
        Brand brand = adminService.updateBrandPrice(id, newSellingPrice);
        log.info("Updated brand price: {}", brand); // Logging the successful price update
        return ResponseEntity.ok(brand);
    }

    @GetMapping("/gains/monthly")
    public ResponseEntity<?> getMonthlyGains() {
        log.info("Fetching monthly gains"); // Logging the request to fetch monthly gains
        List<MonthlyGain> gains = adminService.getMonthlyGains();
        log.info("Monthly gains fetched: {}", gains); // Logging the fetched monthly gains
        return ResponseEntity.ok(gains);
    }

    @GetMapping("/gains/batch/{batchId}")
    public ResponseEntity<?> getBatchGains(@PathVariable Long batchId) {
        log.info("Fetching gains for batch ID: {}", batchId); // Logging the request to fetch batch gains
        BatchGain batchGain = adminService.getBatchGains(batchId);
        log.info("Batch gains fetched: {}", batchGain); // Logging the fetched batch gains
        return ResponseEntity.ok(batchGain);
    }

    @GetMapping("/gains/brand/{brandId}/monthly")
    public ResponseEntity<?> getBrandMonthlyGains(@PathVariable Long brandId) {
        log.info("Fetching monthly gains for brand ID: {}", brandId); // Logging the request to fetch brand's monthly gains
        List<MonthlyGain> gains = adminService.getBrandMonthlyGains(brandId);
        log.info("Brand monthly gains fetched: {}", gains); // Logging the fetched brand monthly gains
        return ResponseEntity.ok(gains);
    }

    @GetMapping("/batches")
    public ResponseEntity<?> getAllBatches() {
        log.info("Fetching all batches"); // Logging the request to fetch all batches
        List<Batch> batches = adminService.getAllBatches();
        log.info("All batches fetched: {}", batches); // Logging the fetched batches
        return ResponseEntity.ok(batches);
    }

    @PostMapping("/batches")
    public ResponseEntity<?> addBatch(@RequestBody BatchDto batchDto) {
        log.info("Adding a new batch: {}", batchDto); // Logging the request to add a new batch
        Batch batch = adminService.addBatch(batchDto);
        log.info("Batch added: {}", batch); // Logging the successful addition of the batch
        return ResponseEntity.status(HttpStatus.CREATED).body(batch);
    }

    @PutMapping("/batches/{id}/complete")
    public ResponseEntity<?> completeBatch(@PathVariable Long id) {
        log.info("Completing batch with ID: {}", id); // Logging the request to complete a batch
        Batch batch = adminService.completeBatch(id);
        log.info("Batch completed: {}", batch); // Logging the successful completion of the batch
        return ResponseEntity.ok(batch);
    }

    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        log.info("Fetching all customers"); // Logging the request to fetch all customers
        List<Customer> customers = adminService.getAllCustomers();
        log.info("All customers fetched: {}", customers); // Logging the fetched customers
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{customerId}/monthly-purchases")
    public ResponseEntity<?> getCustomerMonthlyPurchases(@PathVariable Long customerId) {
        log.info("Fetching monthly purchases for customer ID: {}", customerId); // Logging the request to fetch customer's monthly purchases
        List<MonthlyCustomerPurchaseDto> monthlyPurchases = adminService.getCustomerMonthlyPurchases(customerId);
        log.info("Customer monthly purchases fetched: {}", monthlyPurchases); // Logging the fetched monthly purchases
        return ResponseEntity.ok(monthlyPurchases);
    }

    @GetMapping("/customers/{customerId}/monthly-gains")
    public ResponseEntity<?> getCustomerMonthlyGains(@PathVariable Long customerId) {
        log.info("Fetching monthly gains for customer ID: {}", customerId); // Logging the request to fetch customer's monthly gains
        CustomerMonthlySummaryDto gains = adminService.getCustomerMonthlyGains(customerId);
        log.info("Customer monthly gains fetched: {}", gains); // Logging the fetched monthly gains
        return ResponseEntity.ok(gains);
    }
}