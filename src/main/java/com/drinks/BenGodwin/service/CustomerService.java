package com.drinks.BenGodwin.service;


import com.drinks.BenGodwin.dto.CustomerDto;
import com.drinks.BenGodwin.entity.Customer;
import com.drinks.BenGodwin.exception.ResourceNotFoundException;
import com.drinks.BenGodwin.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@Service
public class CustomerService {

    private CustomerRepository customerRepository;

    public Customer registerCustomer(CustomerDto customerDto) {
        Customer customer = new Customer();
        customer.setName(customerDto.getName());
        customer.setContactInfo(customerDto.getContactInfo());
        return customerRepository.save(customer);
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

}
