package com.example.myapp.controller;

import com.example.myapp.entity.Customer;
import com.example.myapp.exception.custom.*;
import com.example.myapp.exception.custom.model.SuccessResponse;
import com.example.myapp.mapper.CustomerMapper;
import com.example.myapp.model.CustomerRequest;
import com.example.myapp.model.CustomerResponse;
import com.example.myapp.response.ResponseMessage;
import com.example.myapp.service.CustomerService;
import jakarta.validation.Valid;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

/**
 * This package contains the controllers for the customer CURD operation.
 * */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;
    @Autowired
    private CustomerMapper customerMapper;  // Injecting the Mapper
    @GetMapping
    public ResponseEntity<ResponseMessage> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            // If no customers are found, return an error response
            throw new CustomersNotFoundException("No customers found");
        } else {
            // If customers are found, return a success response with the list of customers
            SuccessResponse successResponse = new SuccessResponse("Customers retrieved successfully", customers, HttpStatus.OK);
            // Here, we're passing the list of customers as part of the responseData
            ResponseMessage responseMessage = ResponseMessage.generateResponse(successResponse);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getCustomerById( @PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customer -> {
                    SuccessResponse successResponse = new SuccessResponse("Customer retrieved successfully with id " + id,
                            customerMapper.customerToCustomerResponse(customer),
                            HttpStatus.OK);
                    return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
                }).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id " + id));
    }
/*    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomerResponse = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerResponse);
    }*/

    @PostMapping
    public ResponseEntity<ResponseMessage> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        if (customerRequest == null) {
            throw new InvalidCustomerException("Customer object is null");
        }

        // Map the CustomerRequest to Customer using the CustomerMapper
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);
        Customer savedCustomer = customerService.createCustomer(customer);
        if (savedCustomer != null) {
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(savedCustomer);
            SuccessResponse successResponse
                    = new SuccessResponse("Customer created successfully with id "
                            + customerResponse.getId(), customerResponse, HttpStatus.CREATED);
            return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.CREATED);
        } else {
            throw new CustomerCreationException("Failed to create customer");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
        return customerService.getCustomerById(id)
                .map(customer -> {
                    // Map CustomerRequest to Customer entity
                    Customer customerToUpdate = customerMapper.customerRequestToCustomer(customerRequest);

                    // Update the customer and persist changes
                    Customer updatedCustomer = customerService.updateCustomer(id, customerToUpdate);
                    return Optional.ofNullable(updatedCustomer)
                            .map(updated -> {
                                // Map updated customer to CustomerResponse DTO
                                CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updated);

                                // Generate success response
                                SuccessResponse successResponse
                                        = new SuccessResponse("Customer updated successfully with id "
                                            + id, customerResponse, HttpStatus.OK);
                                return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
                            })
                            .orElseGet(() -> {
                                // Handle case where update failed (customer update failed to persist)
                                throw new CustomerUpdateException("Failed to update customer with id " + id);
                            });
                }).orElseThrow(() -> new CustomerNotFoundException("Could not update. Customer not found with id " + id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteCustomerById(@NonNull @PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customer -> {
                    customerService.deleteCustomerById(id);
                    SuccessResponse successResponse = new SuccessResponse("Customer deleted successfully for id " +id,
                            null, HttpStatus.OK);
                    return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
                }).orElseThrow(() -> new CustomerNotFoundException("Customer not found with id " + id));
    }
}
