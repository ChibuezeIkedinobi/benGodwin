package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.dto.ProfitSalesResponse;
import com.drinks.BenGodwin.entity.Customers;
import com.drinks.BenGodwin.entity.Drinks;
import com.drinks.BenGodwin.entity.Transaction;
import com.drinks.BenGodwin.repository.DrinkRepository;
import com.drinks.BenGodwin.repository.ReceiptRepository;
import com.drinks.BenGodwin.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;


@AllArgsConstructor
@Service
public class TransactionsServiceImpl implements TransactionsService {

    TransactionRepository transactionRepository;

    ReceiptRepository receiptRepository;

    DrinkRepository drinkRepository;

    ReceiptService receiptService;

    @Override
    public ProfitSalesResponse getProfitAndSalesForCustomerInMonth(Customers customer, int year, int month) {
        try {
            LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
            LocalDate lastDayOfMonth = firstDayOfMonth.with(TemporalAdjusters.lastDayOfMonth());

            List<Transaction> customerTransactions = transactionRepository.
                    findByCustomersAndDateBetween(customer, firstDayOfMonth, lastDayOfMonth);

            return calculateProfitAndSales(customerTransactions);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate profit and sales: "+ e.getMessage());
        }
    }


    @Override
    public List<Transaction> getTransactionsForCustomer(Customers customer) {
        try {
            return transactionRepository.findByCustomers(customer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve transactions: "+ e.getMessage());
        }
    }

    @Override
    public Transaction sellDrinksToCustomer(Drinks drink, Customers customer, int quantitySold, BigDecimal sellingPrice) {
        try {
            BigDecimal gain = sellingPrice.subtract(drink.getTotalPurchasePrice())
                    .multiply(BigDecimal.valueOf(quantitySold));

            drink.setQuantityInStock(drink.getQuantityInStock() - quantitySold);

            Transaction transactions = new Transaction();
            transactions.setDrinks(drink);
            transactions.setCustomers(customer);
            transactions.setQuantitySold(quantitySold);
            transactions.setGain(gain);
            transactions.setPurchasePriceAtTransaction(drink.getTotalPurchasePrice());

            transactionRepository.save(transactions);
            drinkRepository.save(drink);

            receiptService.generateReceipt(transactions);
            return transactions;
        } catch (Exception e) {
            throw new RuntimeException("Failed to sell drink: "+ e.getMessage());
        }
    }

    @Override
    public ProfitSalesResponse calculateProfitAndSales(List<Transaction> transactions) {
        try {
            BigDecimal totalProfit = BigDecimal.ZERO;
            int totalSales = 0;

            for (Transaction allTransaction : transactions) {
                totalProfit = totalProfit.add(allTransaction.getGain());
                totalSales += allTransaction.getQuantitySold();
            }

            return new ProfitSalesResponse(totalProfit, totalSales);
        } catch (Exception e) {
            throw new RuntimeException("Failed to calculate profit and sales: "+ e.getMessage());
        }
    }
}
