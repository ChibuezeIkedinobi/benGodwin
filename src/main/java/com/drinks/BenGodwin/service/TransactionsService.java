package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.ProfitSalesResponse;
import com.drinks.BenGodwin.entity.Customers;
import com.drinks.BenGodwin.entity.Drinks;
import com.drinks.BenGodwin.entity.Transaction;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsService {

    ProfitSalesResponse getProfitAndSalesForCustomerInMonth(Customers customer, int year, int month);

    List<Transaction> getTransactionsForCustomer(Customers customer);

    Transaction sellDrinksToCustomer(Drinks drink, Customers customer, int quantitySold, BigDecimal sellingPrice);

    ProfitSalesResponse calculateProfitAndSales(List<Transaction> transactions);

}
