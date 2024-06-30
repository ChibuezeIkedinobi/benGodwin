package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.BulkOrderDto;
import com.drinks.BenGodwin.dto.TransactionItemDto;
import com.drinks.BenGodwin.entity.*;
import com.drinks.BenGodwin.exception.InsufficientStockException;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.BatchRepository;
import com.drinks.BenGodwin.repository.CustomerRepository;
import com.drinks.BenGodwin.repository.TransactionRepository;
import com.drinks.BenGodwin.repository.UsersRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class OrderService {

    private BatchRepository batchRepository;

    private TransactionRepository transactionRepository;

    private CustomerRepository customerRepository;

    private UsersRepository usersRepository;

    @Transactional
    public Transaction processBulkOrder(BulkOrderDto bulkOrderDto) {
        Customer customer = customerRepository.findById(bulkOrderDto.getCustomerId())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        Users cashier = usersRepository.findById(bulkOrderDto.getCashierId())
                .orElseThrow(() -> new ResourceNotFoundException("Cashier not found"));

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<TransactionItem> transactionItems = new ArrayList<>();

        for (TransactionItemDto itemDto : bulkOrderDto.getItems()) {
            Batch batch = batchRepository.findById(itemDto.getBatchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Batch not found"));

            if (batch.getRemainingQuantity() < itemDto.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock for batch ID: " + itemDto.getBatchId());
            }

            batch.setRemainingQuantity(batch.getRemainingQuantity() - itemDto.getQuantity());
            batchRepository.save(batch);

            BigDecimal itemTotal = batch.getBrand().getSellingPrice().multiply(new BigDecimal(itemDto.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);

            TransactionItem transactionItem = new TransactionItem();
            transactionItem.setBrand(batch.getBrand());
            transactionItem.setBatch(batch);
            transactionItem.setQuantity(itemDto.getQuantity());
            transactionItem.setPrice(batch.getBrand().getSellingPrice());
            transactionItems.add(transactionItem);
        }

        BigDecimal discount = bulkOrderDto.getDiscount() != null ? bulkOrderDto.getDiscount() : BigDecimal.ZERO;
        BigDecimal discountedTotal = totalAmount.subtract(discount);

        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setCashier(cashier);
        transaction.setTotalAmount(discountedTotal);
        transaction.setAmountPaid(BigDecimal.ZERO); // To be updated when payment is made
        transaction.setBalance(discountedTotal); // Initial balance equals the total amount after discount
        transaction.setDiscount(discount); // Set discount
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setItems(transactionItems);

        for (TransactionItem item : transactionItems) {
            item.setTransaction(transaction);
        }

        return transactionRepository.save(transaction);
    }

}
