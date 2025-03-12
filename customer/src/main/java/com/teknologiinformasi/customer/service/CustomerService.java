package com.teknologiinformasi.customer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teknologiinformasi.customer.model.Customer;
import com.teknologiinformasi.customer.repository.CustomerRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    // Get all customers
    public List<Customer> getAllCustomer() {
        return customerRepository.findAll();
    }

    // Get a customer by ID
    public Optional<Customer> getCustomerById(Long id) {

        return customerRepository.findById(id);
    }

    // Create a new customer
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    // Update an existing customer
    public Customer updateCustomer(Long id, com.teknologiinformasi.customer.model.Customer customerDetails) {
        com.teknologiinformasi.customer.model.Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan dengan id " + id));

        customer.setName(customerDetails.getName());
        customer.setEmail(customerDetails.getEmail());
        customer.setAddress(customerDetails.getAddress());

        return customerRepository.save(customer);
    }

    // Delete a customer by ID
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer tidak ditemukan dengan id " + id));

        customerRepository.delete(customer);
    }
}
