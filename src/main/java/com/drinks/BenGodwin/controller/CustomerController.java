package com.drinks.BenGodwin.controller;


import com.drinks.BenGodwin.dto.CustomerDto;
import com.drinks.BenGodwin.entity.Customer;
import com.drinks.BenGodwin.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(@RequestBody CustomerDto customerDto) {
        Customer customer = customerService.registerCustomer(customerDto);
        log.info("Customer registered: {}", customer); // Logging the outcome of customer registration
        return ResponseEntity.status(HttpStatus.CREATED).body(customer);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable Long id) {
        Customer customer = customerService.getCustomerById(id);
        log.info("Fetched customer by ID {}: {}", id, customer); // Logging the outcome of fetching customer by ID
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllCustomers() {
        List<Customer> customers = customerService.getAllCustomers();
        log.info("Fetched all customers: {}", customers); // Logging the outcome of fetching all customers
        return ResponseEntity.ok(customers);
    }

}
