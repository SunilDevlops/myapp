package com.example.myapp.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import jakarta.validation.constraints.Pattern;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CustomerRequest {

    private String firstName;
    private String middleName;
    private String lastName;
    private String emailAddress;
    private String phoneNumber;
}