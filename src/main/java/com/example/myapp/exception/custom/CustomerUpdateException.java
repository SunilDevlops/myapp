package com.example.myapp.exception.custom;

public class CustomerUpdateException  extends RuntimeException{
    public CustomerUpdateException(String message) {
        super(message);
    }
}
