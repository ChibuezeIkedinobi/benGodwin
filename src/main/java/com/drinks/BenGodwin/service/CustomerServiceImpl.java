package com.drinks.BenGodwin.service;

import com.drinks.BenGodwin.entity.Customers;
import com.drinks.BenGodwin.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class CustomerServiceImpl implements CustomerService {

    CustomerRepository customerRepository;

    @Override
    public Customers saveCustomer(Customers customer) {
        try {
            return customerRepository.save(customer);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save buyer: " + e.getMessage());
        }
    }

    @Override
    public List<Customers> getAllCustomers() {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve buyers: " + e.getMessage());
        }
    }

    @Override
    public Customers getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found with ID: " + customerId));
    }
}
