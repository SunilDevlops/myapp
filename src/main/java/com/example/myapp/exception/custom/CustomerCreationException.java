package com.example.myapp.exception.custom;

public class CustomerCreationException extends RuntimeException{
    public CustomerCreationException(String message) {
        super(message);
    }
}
