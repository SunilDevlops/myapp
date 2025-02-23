package com.example.myapp.exception;

import com.example.myapp.exception.custom.CustomerNotFoundException;
import com.example.myapp.exception.custom.model.ErrorResponse;
import com.example.myapp.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice // Use @RestControllerAdvice to handle exceptions globally in REST controllers
public class MyAppGlobalExceptionHandling {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.NOT_FOUND);
    }
}
