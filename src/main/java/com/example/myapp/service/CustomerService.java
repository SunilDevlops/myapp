package com.example.myapp.service;

import com.example.myapp.entity.Customer;
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
    public Optional<CustomerResponse> getCustomerById(Long id) {
        Optional<Customer> customer = customerRepository.findById(id);
        return customer.map(customerMapper::customerToCustomerResponse);
    }

    public Optional<Customer> getCustomerByEmail(String emailAddress) {
        return customerRepository.findByEmailAddress(emailAddress);
    }

    @Transactional
    public CustomerResponse createCustomer(CustomerRequest customerRequest) {
        // Map CustomerResponse to Customer entity
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);
        System.out.println("Going to print");
        // Save customer to the repository or perform other business logic
        Customer createdCustomer =  customerRepository.save(customer);
        System.out.println("Done printint");

        //customerRepository.flush();
        return customerMapper.customerToCustomerResponse(createdCustomer);
    }
    @Transactional
    public CustomerResponse updateCustomer(CustomerRequest customerDetails) {

        //Customer updateCustomer = customerMapper.customerRequestToCustomer(customerDetails);
        Customer customer = customerRepository.findById(customerDetails.getId())
                .map(existingCustomer -> {
            // Perform the update if customer exists
            existingCustomer.setFirstName(customerDetails.getFirstName());
            existingCustomer.setMiddleName(customerDetails.getMiddleName());
            existingCustomer.setLastName(customerDetails.getLastName());
            existingCustomer.setEmailAddress(customerDetails.getEmailAddress());
            existingCustomer.setPhoneNumber(customerMapper.mapPhoneNumber(customerDetails.getPhoneNumber()));            // Save the updated customer back to the database
            return customerRepository.save(existingCustomer);
        }).orElseThrow(() -> new RuntimeException("Customer not found"));
        return customerMapper.customerToCustomerResponse(customer);

    }
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customerRepository.delete(customer);
    }
}
