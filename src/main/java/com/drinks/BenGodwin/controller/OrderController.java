package com.drinks.BenGodwin.controller;


import com.drinks.BenGodwin.dto.BulkOrderDto;
import com.drinks.BenGodwin.entity.Transaction;
import com.drinks.BenGodwin.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private OrderService orderService;

    @PostMapping("/bulk")
    public ResponseEntity<?> processBulkOrder(@RequestBody BulkOrderDto bulkOrderDto) {
        Transaction transaction = orderService.processBulkOrder(bulkOrderDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
    }

}
