package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.entity.Customers;

import java.util.List;

public interface CustomerService{

    Customers saveCustomer(Customers customers);

    List<Customers> getAllCustomers();

    Customers getCustomerById(Long customerId);
}
