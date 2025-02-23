package com.example.myapp.service;

import com.example.myapp.entity.Customer;
import com.example.myapp.exception.custom.CustomerNotFoundException;
import com.example.myapp.exception.custom.CustomersNotFoundException;
import com.example.myapp.mapper.CustomerMapper;
import com.example.myapp.model.CustomerRequest;
import com.example.myapp.model.CustomerResponse;
import com.example.myapp.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CustomerService {
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

/*    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        // Map CustomerResponse to Customer entity
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);
        // Save customer to the repository or perform other business logic
        Customer createdCustomer =  customerRepository.save(customer);

        return customerMapper.customerToCustomerResponse(createdCustomer);
    }*/
    @Transactional
    public Customer createCustomer(Customer customer) {
        // Map CustomerResponse to Customer entity
        // Save customer to the repository or perform other business logic
        return Optional.ofNullable(customer)
                .map(customerRepository::save)
                .orElseThrow(() -> new RuntimeException("Failed while saving the customer in DB"));

    }
    @Transactional
    public Customer updateCustomer(Long id, Customer customerDetails) {
        return customerRepository.findById(id)
                .map(existingCustomer -> {
                    // Update fields only if new values are provided, otherwise keep the existing values
                    Optional.ofNullable(customerDetails.getFirstName()).ifPresent(existingCustomer::setFirstName);
                    Optional.ofNullable(customerDetails.getMiddleName()).ifPresent(existingCustomer::setMiddleName);
                    Optional.ofNullable(customerDetails.getLastName()).ifPresent(existingCustomer::setLastName);
                    Optional.ofNullable(customerDetails.getEmailAddress()).ifPresent(existingCustomer::setEmailAddress);
                    Optional.ofNullable(customerDetails.getPhoneNumber()).ifPresent(existingCustomer::setPhoneNumber);
                    return customerRepository.save(existingCustomer);
                }).orElseThrow(() -> new CustomerNotFoundException("Could not update. Customer not found for id "+customerDetails.getId()));
    }
    @Transactional
    public void deleteCustomerById(Long id) {
        customerRepository.deleteById(id);
    }
}
