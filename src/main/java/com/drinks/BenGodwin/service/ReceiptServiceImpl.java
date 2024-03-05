package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Receipt;
import com.drinks.BenGodwin.entity.Transaction;
import com.drinks.BenGodwin.repository.ReceiptRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@AllArgsConstructor
@Service
public class ReceiptServiceImpl implements ReceiptService {

    ReceiptRepository receiptRepository;

    @Override
    public Receipt generateReceipt(Transaction transaction) {
        try {
            Receipt receipt = new Receipt();
            receipt.setTransaction(transaction);

            return receiptRepository.save(receipt);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate receipt: "+ e.getMessage());
        }
    }
}
