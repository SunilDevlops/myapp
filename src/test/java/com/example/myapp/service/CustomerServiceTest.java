package com.example.myapp.service;

import com.example.myapp.entity.Customer;
import com.example.myapp.entity.PhoneNumber;
import com.example.myapp.exception.MyAppGlobalExceptionHandling;
import com.example.myapp.exception.custom.CustomerNotFoundException;
import com.example.myapp.mapper.CustomerMapper;
import com.example.myapp.model.CustomerResponse;
import com.example.myapp.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Optional;

public class CustomerServiceTest {
    private MockMvc mockMvc;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Initialize mocks
        mockMvc = MockMvcBuilders.standaloneSetup(customerService)
                .setControllerAdvice(new MyAppGlobalExceptionHandling()) // Make sure the handler is applied
                .build();
        customer = new Customer();
        customer.setId(1L);
        customer.setVersion(0L);
        customer.setFirstName("John");
        customer.setMiddleName("");
        customer.setLastName("Doe");
        customer.setEmailAddress("john.doe@example.com");
        customer.setPhoneNumber(new PhoneNumber("+1", "234", "567", "8907"));
    }

    // Test for getAllCustomers method
    @Test
    void testGetAllCustomers() {
        // Given
        CustomerResponse customerResponse = new CustomerResponse();
        customerResponse.setFirstName("John");
        customerResponse.setLastName("Doe");
        customerResponse.setEmailAddress("john.doe@example.com");
        when(customerRepository.findAll()).thenReturn(Arrays.asList(customer));
        when(customerMapper.customerToCustomerResponse(customer)).thenReturn(customerResponse);

        // When
        var customerResponses = customerService.getAllCustomers();

        // Then
        assertNotNull(customerResponses);
        assertEquals(1, customerResponses.size());
        assertEquals("John", customerResponses.get(0).getFirstName());
        verify(customerRepository, times(1)).findAll();
    }

    // Test for getCustomerById method (Customer Found)
    @Test
    void testGetCustomerById_Found() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        // When
        Optional<Customer> foundCustomer = customerService.getCustomerById(1L);

        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals("John", foundCustomer.get().getFirstName());
        verify(customerRepository, times(1)).findById(1L);
    }

    // Test for getCustomerById method (Customer Not Found)
    @Test
    void testGetCustomerById_NotFound() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<Customer> foundCustomer = customerService.getCustomerById(1L);

        // Then
        assertFalse(foundCustomer.isPresent());
        verify(customerRepository, times(1)).findById(1L);
    }

    // Test for createCustomer method
    @Test
    void testCreateCustomer() {
        // Given
        when(customerRepository.save(customer)).thenReturn(customer);

        // When
        Customer createdCustomer = customerService.createCustomer(customer);

        // Then
        assertNotNull(createdCustomer);
        assertEquals("John", createdCustomer.getFirstName());
        verify(customerRepository, times(1)).save(customer);
    }

    // Test for updateCustomer method (Customer Found)
    @Test
    void testUpdateCustomer_Found() {
        // Given
        Customer updatedCustomer = new Customer();
        updatedCustomer.setId(1L);
        updatedCustomer.setVersion(0L);
        updatedCustomer.setFirstName("Jane");
        updatedCustomer.setMiddleName("");
        updatedCustomer.setLastName("Doe");
        updatedCustomer.setEmailAddress("jane.doe@example.com");
        updatedCustomer.setPhoneNumber(new PhoneNumber("+1", "234", "567", "1234"));
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(customer)).thenReturn(updatedCustomer);

        // When
        Customer returnedCustomer = customerService.updateCustomer(1L, updatedCustomer);

        // Then
        assertNotNull(returnedCustomer);
        assertEquals("Jane", returnedCustomer.getFirstName());
        verify(customerRepository, times(1)).findById(1L);
        verify(customerRepository, times(1)).save(updatedCustomer);
    }

    // Test for updateCustomer method (Customer Not Found)
    @Test
    void testUpdateCustomer_NotFound() {
        // Given
        Customer updatedCustomer = new Customer();
        updatedCustomer.setFirstName("Jane");

        when(customerRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(CustomerNotFoundException.class, () -> customerService.updateCustomer(1L, updatedCustomer));
        verify(customerRepository, times(1)).findById(1L);
    }

    // Test for deleteCustomerById method (Customer Found)
    @Test
    void testDeleteCustomerById_Found() {
        customerService.deleteCustomerById(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }


}
