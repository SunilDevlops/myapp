package com.example.myapp.exception.custom;

public class InvalidCustomerException extends RuntimeException{
    public InvalidCustomerException(String message) {
        super(message);
    }
}
