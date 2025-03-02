package com.example.myapp.controller;

import com.example.myapp.entity.Customer;
import com.example.myapp.entity.PhoneNumber;
import com.example.myapp.exception.MyAppGlobalExceptionHandling;
import com.example.myapp.exception.custom.CustomerNotFoundException;
import com.example.myapp.mapper.CustomerMapper;
import com.example.myapp.model.CustomerRequest;
import com.example.myapp.model.CustomerResponse;
import com.example.myapp.response.ResponseMessage;
import com.example.myapp.service.CustomerService;
import com.example.myapp.exception.custom.model.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerController customerController;

    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController)
                .setControllerAdvice(new MyAppGlobalExceptionHandling()) // Make sure the handler is applied
                .build();
    }

    @Test
    void testGetAllCustomers() throws Exception {
        // Given
        CustomerResponse customerResponse = new CustomerResponse(1L, "John","", "Doe", "john.doe@example.com","+1 213 564 7869");
        when(customerService.getAllCustomers()).thenReturn(Collections.singletonList(customerResponse));

        // When & Then
        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseData.data[0].id").value(1L))
                .andExpect(jsonPath("$.responseData.data[0].firstName").value("John"))
                .andExpect(jsonPath("$.responseData.data[0].emailAddress").value("john.doe@example.com"));
    }

    @Test
    void testGetCustomerById() throws Exception {
        // Given
        Customer customer = new Customer(1L, 0L, "John","","Doe", "john.doe@example.com",new PhoneNumber("+1", "213", "564","7869"));
        CustomerResponse customerResponse = new CustomerResponse(1L, "John","", "Doe", "john.doe@example.com","+1 213 564 7869");

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        // When & Then
        mockMvc.perform(get("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseData.data.id").value(1L))
                .andExpect(jsonPath("$.responseData.data.firstName").value("John"))
                .andExpect(jsonPath("$.responseData.data.emailAddress").value("john.doe@example.com"));
    }

    @Test
    void testGetCustomerById_NotFound() throws Exception {
        // Given
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/customers/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.responseData.message").value("Customer not found with id 1"));

    }

    @Test
    void testCreateCustomer() throws Exception {
        // Given
        CustomerRequest customerRequest = new CustomerRequest("John","","Doe", "john.doe@example.com","+1 213-564-7869");
        Customer customer = new Customer(1L,1L,  "John","","Doe", "john.doe@example.com",new PhoneNumber("+1", "213", "564","7869"));
        CustomerResponse customerResponse = new CustomerResponse(1L, "John","", "Doe", "john.doe@example.com","+1 213-564-7869");

        when(customerMapper.customerRequestToCustomer(customerRequest)).thenReturn(customer);
        when(customerService.createCustomer(customer)).thenReturn(customer);
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        // When & Then
        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"middleName\": \"\", \"lastName\": \"Doe\", \"emailAddress\": \"john.doe@example.com\" , \"phoneNumber\": \"+1 213-564-7869\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.responseData.data.id").value(1L))
                .andExpect(jsonPath("$.responseData.data.firstName").value("John"))
                .andExpect(jsonPath("$.responseData.data.emailAddress").value("john.doe@example.com"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        // Given
        CustomerRequest customerRequest = new CustomerRequest("John","updated","Doe", "john.doe.updated@example.com","+1 213-564-7869");
        Customer existingCustomer = new Customer(1L, 0L, "John","","Doe", "john.doe@example.com",new PhoneNumber("+1", "213", "564","7869"));
        Customer updatedCustomer = new Customer(1L, 1L, "John","updated","Doe", "john.doe.updated@example.com",new PhoneNumber("+1", "213", "564","7869"));
        CustomerResponse customerResponse = new CustomerResponse(1L, "John","updated", "Doe", "john.doe.updated@example.com","+1 213-564-7869");

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(existingCustomer));
        when(customerMapper.customerRequestToCustomer(customerRequest)).thenReturn(updatedCustomer);
        when(customerService.updateCustomer(1L, updatedCustomer)).thenReturn(updatedCustomer);
        when(customerMapper.customerToCustomerResponse(updatedCustomer)).thenReturn(customerResponse);

        // When & Then
        mockMvc.perform(put("/api/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"firstName\": \"John\", \"middleName\": \"updated\",\"lastName\": \"Doe\", \"emailAddress\": \"john.doe.updated@example.com\" , \"phoneNumber\": \"+1 213-564-7869\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseData.data.id").value(1L))
                .andExpect(jsonPath("$.responseData.data.firstName").value("John"))
                .andExpect(jsonPath("$.responseData.data.middleName").value("updated"))
                .andExpect(jsonPath("$.responseData.data.lastName").value("Doe"))
                .andExpect(jsonPath("$.responseData.data.emailAddress").value("john.doe.updated@example.com"));
    }

    @Test
    void testDeleteCustomerById() throws Exception {
        // Given
        Customer customer = new Customer(1L, 0L, "John","","Doe", "john.doe@example.com",new PhoneNumber("+1", "213", "564","7869"));

        when(customerService.getCustomerById(1L)).thenReturn(Optional.of(customer));

        // When & Then
        mockMvc.perform(delete("/api/customers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.responseData.message").value("Customer deleted successfully for id 1"));
    }

    @Test
    void testDeleteCustomerById_NotFound() throws Exception {
        // Given
        when(customerService.getCustomerById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(delete("/api/customers/{id}", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.responseData.message").value("Customer not found with id 1"));
    }
}
