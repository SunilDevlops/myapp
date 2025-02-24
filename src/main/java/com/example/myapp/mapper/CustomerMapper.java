package com.example.myapp.mapper;

import com.example.myapp.entity.Customer;
import com.example.myapp.entity.PhoneNumber;
import com.example.myapp.model.CustomerRequest;
import com.example.myapp.model.CustomerResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    CustomerMapper INSTANCE = Mappers.getMapper(CustomerMapper.class);
    // Mapping from entity to response model
    @Mapping(target = "phoneNumber", expression = "java(concatenatePhoneNumber(customer.getPhoneNumber()))")
    CustomerResponse customerToCustomerResponse(Customer customer);

    // Custom mapping method to concatenate phone number fields
    @org.mapstruct.Named("concatenatePhoneNumber")
    default String concatenatePhoneNumber(PhoneNumber phoneNumber) {
        // Ensure none of the components are null, and concatenate them with hyphens
        if (phoneNumber == null
                || phoneNumber.getCountryCode() == null
                || phoneNumber.getAreaCode() == null
                || phoneNumber.getPrefix() == null
                || phoneNumber.getLineNumber() == null) {
            return null;
        }
        return phoneNumber.getCountryCode() + " " + phoneNumber.getAreaCode() + "-" + phoneNumber.getPrefix() + "-" + phoneNumber.getLineNumber();
    }
    // Mapping from request model to entity
    @Mapping(target = "phoneNumber", expression = "java(mapPhoneNumber(customerRequest.getPhoneNumber()))")
    Customer customerRequestToCustomer(CustomerRequest customerRequest);


    // Helper method to split the phone number and map it to a PhoneNumber object
    @org.mapstruct.Named("mapPhoneNumber")
    default PhoneNumber mapPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || !phoneNumber.startsWith("+")) {
            return null; // Handle null or invalid format
        }

        // Remove the "+" at the beginning and split based on space
        String[] parts = phoneNumber.substring(1).split(" "); // Split into countryCode and rest

        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        String countryCode = "+" + parts[0]; // First part is the countryCode
        String restOfNumber = parts[1]; // Second part is the rest of the number

        // Split the rest of the number based on the "-" character
        String[] numberParts = restOfNumber.split("-");
        if (numberParts.length != 3) {
            throw new IllegalArgumentException("Invalid phone number format");
        }

        String areaCode = numberParts[0];  // The area code
        String prefix = numberParts[1];    // The prefix
        String lineNumber = numberParts[2]; // The line number

        // Create a PhoneNumber object and return it
        PhoneNumber phone = new PhoneNumber();
        phone.setCountryCode(countryCode); // Set the countryCode
        phone.setAreaCode(areaCode);       // Set the areaCode
        phone.setPrefix(prefix);           // Set the prefix
        phone.setLineNumber(lineNumber);   // Set the lineNumber
        return phone;
    }
}
