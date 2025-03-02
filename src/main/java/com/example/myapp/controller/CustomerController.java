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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

/**
 * This package contains the controllers for the customer CURD operation.
 * */
@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @Autowired
    CustomerService customerService;
    @Autowired
    private CustomerMapper customerMapper;  // Injecting the Mapper
    @GetMapping
    public ResponseEntity<ResponseMessage> getAllCustomers() {
        logger.info("Calling getAllCustomers....");
        List<CustomerResponse> customers = customerService.getAllCustomers();
        if (customers.isEmpty()) {
            logger.error("No customers found");
            // If no customers are found, return an error response
            throw new CustomersNotFoundException("No customers found");
        } else {
            logger.info("Customers are found of totalsize - "+customers.size());
            // If customers are found, return a success response with the list of customers
            SuccessResponse successResponse = new SuccessResponse("Customers retrieved successfully", customers, HttpStatus.OK);
            logger.debug("Sending list of customers as part of the responseData");
            // Here, we're passing the list of customers as part of the responseData
            ResponseMessage responseMessage = ResponseMessage.generateResponse(successResponse);
            return new ResponseEntity<>(responseMessage, HttpStatus.OK);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseMessage> getCustomerById( @PathVariable Long id) {
        logger.info("Calling getCustomerById....");
        return customerService.getCustomerById(id)
            .map(customer -> {
                logger.info("Customer retrieved successfully with id " + id);
                SuccessResponse successResponse = new SuccessResponse("Customer retrieved successfully with id " + id,
                        customerMapper.customerToCustomerResponse(customer),
                        HttpStatus.OK);
                logger.debug("Got customer resposne for id :" +id);
                return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
            }).orElseThrow(() -> {
                logger.error("Customer not found with id " + id);
                throw new CustomerNotFoundException("Customer not found with id " + id);
            });
    }
    @PostMapping
    public ResponseEntity<ResponseMessage> createCustomer(@Valid @RequestBody CustomerRequest customerRequest) {
        logger.info("Calling createCustomer....");
        if (customerRequest == null) {
            logger.error("Customer object is null");
            throw new InvalidCustomerException("Customer object is null");
        }

        // Map the CustomerRequest to Customer using the CustomerMapper
        logger.info("Map the CustomerRequest to Customer using the CustomerMapper");
        Customer customer = customerMapper.customerRequestToCustomer(customerRequest);
        logger.info("Going to creating customer");
        Customer savedCustomer = customerService.createCustomer(customer);
        if (savedCustomer != null) {
            logger.info("Converting the Customer to CustomerResponse using the CustomerMapper");
            CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(savedCustomer);
            SuccessResponse successResponse
                    = new SuccessResponse("Customer created successfully with id "
                            + customerResponse.getId(), customerResponse, HttpStatus.CREATED);
            logger.debug("Customer created successfully with id "+customerResponse.getId());
            return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.CREATED);
        } else {
            logger.error("Failed to create customer");
            throw new CustomerCreationException("Failed to create customer");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseMessage> updateCustomer(@PathVariable Long id, @RequestBody CustomerRequest customerRequest) {
        logger.info("Calling updateCustomer....");
        return customerService.getCustomerById(id)
                .map(customer -> {
                    logger.info("Map the CustomerRequest to Customer using the CustomerMapper");
                    // Map CustomerRequest to Customer entity
                    Customer customerToUpdate = customerMapper.customerRequestToCustomer(customerRequest);
                    logger.info("Going to update customer");
                    // Update the customer and persist changes
                    Customer updatedCustomer = customerService.updateCustomer(id, customerToUpdate);
                    return Optional.ofNullable(updatedCustomer)
                            .map(updated -> {
                                logger.info("Converting the Customer to CustomerResponse using the CustomerMapper");
                                // Map updated customer to CustomerResponse DTO
                                CustomerResponse customerResponse = customerMapper.customerToCustomerResponse(updated);

                                // Generate success response
                                SuccessResponse successResponse
                                        = new SuccessResponse("Customer updated successfully with id "
                                            + id, customerResponse, HttpStatus.OK);
                                logger.debug("Customer updated successfully with id "+id);
                                return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
                            })
                            .orElseGet(() -> {
                                logger.error("Failed to update customer with id " + id);
                                // Handle case where update failed (customer update failed to persist)
                                throw new CustomerUpdateException("Failed to update customer with id " + id);
                            });
                }).orElseThrow(() -> {
                    logger.error("Could not update. Customer not found with id " + id);
                    throw new CustomerNotFoundException("Could not update. Customer not found with id " + id);
                });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteCustomerById(@NonNull @PathVariable Long id) {
        logger.info("Calling deleteCustomerById....");
        return customerService.getCustomerById(id)
                .map(customer -> {
                    logger.info("Customer retrieved successfully with id " + id);
                    customerService.deleteCustomerById(id);
                    logger.info("Customer retrieved successfully with id " + id);
                    SuccessResponse successResponse = new SuccessResponse("Customer deleted successfully for id " +id,
                            null, HttpStatus.OK);
                    logger.debug("Customer deleted successfully with id " + id);
                    return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
                }).orElseThrow(() -> {
                    logger.error("Customer not found with id " + id);
                    throw new CustomerNotFoundException("Customer not found with id " + id);
                });
    }

    @GetMapping("/api/getPodDetails")
    public String sayHello() throws UnknownHostException {
        String podName = System.getenv("HOSTNAME");  // Get the pod name
        logger.debug("Podname:  " + podName);
        return "Hello from pod: " + podName;
    }
}
