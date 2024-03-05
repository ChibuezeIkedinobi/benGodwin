package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.entity.Receipt;
import com.drinks.BenGodwin.entity.Transaction;

public interface ReceiptService {

    Receipt generateReceipt(Transaction transaction);

}
