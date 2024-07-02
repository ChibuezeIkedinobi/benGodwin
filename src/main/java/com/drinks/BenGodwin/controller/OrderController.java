package com.drinks.BenGodwin.controller;


import com.drinks.BenGodwin.dto.BulkOrderDto;
import com.drinks.BenGodwin.entity.Transaction;
import com.drinks.BenGodwin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/")
    public ResponseEntity<?> processOrder(@RequestBody BulkOrderDto bulkOrderDto) {
        Transaction transaction = orderService.processOrder(bulkOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

    @GetMapping("/customers/{customerId}/balance")
    public ResponseEntity<?> getCustomerBalance(@PathVariable Long customerId) {
        BigDecimal balance = orderService.getCustomerBalance(customerId);
        return ResponseEntity.ok(balance);
    }

}
