package com.example.myapp.controller;

import com.example.myapp.entity.Customer;
import com.example.myapp.exception.custom.CustomerNotFoundException;
import com.example.myapp.exception.custom.model.SuccessResponse;
import com.example.myapp.exception.custom.model.ErrorResponse;

import com.example.myapp.model.CustomerRequest;
import com.example.myapp.model.CustomerResponse;
import com.example.myapp.response.ResponseData;
import com.example.myapp.response.ResponseHeader;
import com.example.myapp.response.ResponseMessage;
import com.example.myapp.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {
    @Autowired
    CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        List<CustomerResponse> customers = customerService.getAllCustomers();
        return new ResponseEntity<>(customers, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable Long id) {
        return customerService.getCustomerById(id)
                .map(customer -> ResponseEntity.ok(customer))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
    @PostMapping
    public ResponseEntity<CustomerResponse> createCustomer(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse createdCustomerResponse = customerService.createCustomer(customerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCustomerResponse);
    }

   @PatchMapping
    public ResponseEntity<CustomerResponse> updateCustomer(@RequestBody CustomerRequest customerRequest) {
       CustomerResponse createdCustomerResponse = customerService.updateCustomer(customerRequest);

       return ResponseEntity.ok(createdCustomerResponse);
    }

/*    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }*/
/*    @DeleteMapping("/{id}")
    public ResponseEntity<SuccessResponse> deleteCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustById(id);
        if (customer.isPresent()) {
            customerService.deleteCustomerById(id);
            SuccessResponse successResponse = new SuccessResponse(
                    "Customer deleted successfully",
                    "200 OK",
                    new java.util.Date().toString(),
                    "success",
                    "Customer with id " + id + " got deleted"
                    );
            return new ResponseEntity<>(successResponse, HttpStatus.OK);
        } else {

            throw new CustomerNotFoundException("Customer not found with id " + id);
        }
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseMessage> deleteCustomerById(@PathVariable Long id) {
        Optional<Customer> customer = customerService.getCustById(id);
        if (customer.isPresent()) {
            customerService.deleteCustomerById(id);
            // Generate success response using the generic method
            SuccessResponse successResponse = new SuccessResponse("Customer deleted successfully for id " +id, HttpStatus.OK);
            return new ResponseEntity<>(ResponseMessage.generateResponse(successResponse), HttpStatus.OK);
        } else {
            // Handle case where customer is not found (return error response)
            throw new CustomerNotFoundException("Customer not found with id " + id);
        }
    }


}
