package com.example.myapp.exception.custom;

public class CustomersNotFoundException extends RuntimeException{
    public CustomersNotFoundException(String message) {
        super(message);
    }

}
