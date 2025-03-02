package com.example.myapp.service;

import com.example.myapp.controller.CustomerController;
import com.example.myapp.entity.Customer;
import com.example.myapp.exception.custom.CustomerNotFoundException;
import com.example.myapp.exception.custom.CustomersNotFoundException;
import com.example.myapp.mapper.CustomerMapper;
import com.example.myapp.model.CustomerRequest;
import com.example.myapp.model.CustomerResponse;
import com.example.myapp.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private CustomerMapper customerMapper;  // Injecting the Mapper

    public List<CustomerResponse> getAllCustomers() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList.stream()
                .map(customerMapper::customerToCustomerResponse) // Using Mapper
                .collect(Collectors.toList());
    }
    public Optional<Customer> getCustomerById(Long id) {
        return customerRepository.findById(id);
    }
    public Optional<Customer> getCustomerByEmail(String emailAddress) {
        return customerRepository.findByEmailAddress(emailAddress);
    }
    @Transactional
    public Customer createCustomer(Customer customer) {
        // Map CustomerResponse to Customer entity
        // Save customer to the repository or perform other business logic
        return Optional.ofNullable(customer)
                .map(customerRepository::save)
                .orElseThrow(() -> {
                    logger.error("Failed while saving the customer in DB");
                   throw new RuntimeException("Failed while saving the customer in DB");
                });

    }
    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    logger.info("Update fields only if new values are provided, otherwise keep the existing values");
                    // Update fields only if new values are provided, otherwise keep the existing values
                    Optional.ofNullable(customerDetails.getFirstName()).ifPresent(existingCustomer::setFirstName);
                    Optional.ofNullable(customerDetails.getMiddleName()).ifPresent(existingCustomer::setMiddleName);
                    Optional.ofNullable(customerDetails.getLastName()).ifPresent(existingCustomer::setLastName);
                    Optional.ofNullable(customerDetails.getEmailAddress()).ifPresent(existingCustomer::setEmailAddress);
                    Optional.ofNullable(customerDetails.getPhoneNumber()).ifPresent(existingCustomer::setPhoneNumber);
                    return customerRepository.save(existingCustomer);
                }).orElseThrow(() -> {
                    logger.error("Could not update. Customer not found for id "+customerDetails.getId());
                    throw new CustomerNotFoundException("Could not update. Customer not found for id "+customerDetails.getId());
                });
    }
    @Transactional
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);

    }
}
