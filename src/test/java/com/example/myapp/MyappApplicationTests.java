package com.example.myapp;

import com.example.myapp.model.CustomerRequest;
import com.example.myapp.repository.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class MyappApplicationTests {
	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testGetAllCustomers() throws Exception {
		// Act and Assert
		mockMvc.perform(get("/api/customers"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseData.data", hasSize(7)))  // Assuming data.sql has one customer
				.andExpect(jsonPath("$.responseData.data[6].firstName", is("John")))
				.andExpect(jsonPath("$.responseData.data[6].lastName", is("Smith")));
	}

	@Test
	public void testGetCustomerById() throws Exception {
		// Act and Assert
		mockMvc.perform(get("/api/customers/{id}", 6L))  // Assuming the customer ID in data.sql is 1
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseData.data.firstName", is("Jane")))
				.andExpect(jsonPath("$.responseData.data.lastName", is("Doe")));
	}

	@Test
	public void testCreateCustomer() throws Exception {
		// Arrange: Create a new customer request object
		CustomerRequest newCustomerRequest = CustomerRequest.builder()
				.firstName("Bryan")
				.middleName(null)  // Optional middle name, can be omitted or set to null
				.lastName("Adams")
				.emailAddress("bryan.adams@example.com")
				.phoneNumber("+1 813-453-7234")
				.build();
		// Act and Assert
		mockMvc.perform(post("/api/customers")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(newCustomerRequest)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.responseData.data.firstName", is("Bryan")))
				.andExpect(jsonPath("$.responseData.data.lastName", is("Adams")));
	}

	@Test
	public void testUpdateCustomer() throws Exception {
		// Arrange: Update customer data
		CustomerRequest updatedCustomerRequest = CustomerRequest.builder()
				.firstName("Johnny")
				.middleName(null)  // Optional middle name, can be omitted or set to null
				.lastName("Smith")
				.emailAddress("johnny.smith@example.com")
				.phoneNumber("+1 240-456-7890")
				.build();

		// Act and Assert
		mockMvc.perform(put("/api/customers/{id}", 1L)  // Assuming the customer ID in data.sql is 1
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(updatedCustomerRequest)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseData.data.firstName", is("Johnny")))
				.andExpect(jsonPath("$.responseData.data.lastName", is("Smith")));
	}

	@Test
	public void testDeleteCustomer() throws Exception {
		// Act and Assert
		mockMvc.perform(delete("/api/customers/{id}", 1L))  // Assuming the customer ID in data.sql is 1
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.responseData.message", is("Customer deleted successfully for id 1")));
	}

	@Test
	public void testGetCustomerByIdNotFound() throws Exception {
		// Act and Assert
		mockMvc.perform(get("/api/customers/{id}", 9999L))  // Assuming 9999L doesn't exist
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.responseData.message", is("Customer not found with id 9999")));
	}

	@Test
	public void testCreateCustomerBadRequest() throws Exception {
		// Arrange: Create an invalid customer request (missing first name)
		CustomerRequest invalidCustomerRequest = CustomerRequest.builder()
				.firstName("")
				.middleName(null)  // Optional middle name, can be omitted or set to null
				.lastName("Doe")
				.emailAddress("doe@gmail.com")
				.phoneNumber("+1 813-564-3232")
				.build();
		// Act and Assert
		mockMvc.perform(post("/api/customers")
						.contentType("application/json")
						.content(objectMapper.writeValueAsString(invalidCustomerRequest)))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.responseData.message")
						.value("Validation failed : [firstName: First name is required field]"));
	}
}
