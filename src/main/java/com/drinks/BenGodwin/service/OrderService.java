package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.BulkOrderDto;
import com.drinks.BenGodwin.dto.TransactionItemDto;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class OrderService {

    private final BrandRepository brandRepository;
    private final BatchRepository batchRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final UsersRepository usersRepository;

    public Transaction processOrder(BulkOrderDto bulkOrderDto) {
        Customer customer = customerRepository.findById(bulkOrderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Users cashier = usersRepository.findById(bulkOrderDto.getCashierId())
                .orElseThrow(() -> new ResourceNotFoundException("Cashier not found"));

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setCashier(cashier);
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDiscount(bulkOrderDto.getDiscount());

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<TransactionItem> transactionItems = new ArrayList<>();
        for (TransactionItemDto itemDto : bulkOrderDto.getItems()) {
            Brand brand = brandRepository.findById(itemDto.getBrandId())
                    .orElseThrow(() -> new ResourceNotFoundException("Brand not found"));

            // Find the batch associated with the brand
            Batch batch = batchRepository.findFirstByBrandOrderByCreatedAtAsc(brand)
                    .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setTransaction(transaction);
            transactionItem.setBrand(brand);
            transactionItem.setBatch(batch);
            transactionItem.setQuantity(itemDto.getQuantity());
            transactionItem.setUnitPrice(brand.getSellingPrice());
            transactionItem.setTotalPrice(brand.getSellingPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));

            totalAmount = totalAmount.add(transactionItem.getTotalPrice());
            transactionItems.add(transactionItem);
        }

        totalAmount = totalAmount.subtract(bulkOrderDto.getDiscount());
        BigDecimal totalAmountPaid = bulkOrderDto.getAmountPaid();
        BigDecimal newBalance = totalAmount.subtract(totalAmountPaid).add(customer.getBalance());

        transaction.setTotalAmount(totalAmount);
        transaction.setAmountPaid(totalAmountPaid);
        transaction.setBalance(newBalance);
        transaction.setPaid(newBalance.compareTo(BigDecimal.ZERO) <= 0);
        transaction.setItems(transactionItems);

        // Update customer's balance
        customer.setBalance(newBalance);

        Transaction savedTransaction = transactionRepository.save(transaction);
        customerRepository.save(customer);
        log.info("Order processed: {}", savedTransaction); // Logging the outcome of processing an order
        return savedTransaction;
    }

    public BigDecimal getCustomerBalance(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        log.info("Fetched customer balance for ID {}: {}", customerId, customer.getBalance()); // Logging the outcome of fetching customer balance
        return customer.getBalance();
    }
}