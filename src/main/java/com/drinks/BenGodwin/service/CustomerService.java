package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.CustomerDto;
import com.drinks.BenGodwin.entity.Customer;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public Customer registerCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setContactInfo(customerDto.getContactInfo());
        Customer savedCustomer = customerRepository.save(customer);
        log.info("Customer registered: {}", savedCustomer); // Logging the outcome of customer registration
        return savedCustomer;
    }

    public Customer getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        log.info("Fetched customer by ID {}: {}", id, customer); // Logging the outcome of fetching customer by ID
        return customer;
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = customerRepository.findAll();
        log.info("All customers fetched: {}", customers); // Logging the outcome of fetching all customers
        return customers;
    }

}
