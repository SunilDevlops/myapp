package com.example.myapp.exception;

import com.example.myapp.exception.custom.*;
import com.example.myapp.exception.custom.model.ErrorResponse;
import com.example.myapp.exception.custom.model.SuccessResponse;
import com.example.myapp.response.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice // Use @RestControllerAdvice to handle exceptions globally in REST controllers
public class MyAppGlobalExceptionHandling {

/*    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),null,  HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.NOT_FOUND);
    }*/
    @ExceptionHandler(CustomerNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleCustomerNotFoundException(CustomerNotFoundException ex) {
        ResponseMessage responseMessage = ResponseMessage.generateResponse(new SuccessResponse(
                ex.getMessage(), null, HttpStatus.NOT_FOUND));
        return new ResponseEntity<>(responseMessage, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomersNotFoundException.class)
    public ResponseEntity<ResponseMessage> handleCustomersNotFoundException(CustomersNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),null,  HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CustomerUpdateException.class)
    public ResponseEntity<ResponseMessage> handleCustomerUpdateException(CustomerUpdateException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),null,  HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(CustomerCreationException.class)
    public ResponseEntity<ResponseMessage> handleCustomerCreationException  (CustomerCreationException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),null,  HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(InvalidCustomerException.class)
    public ResponseEntity<ResponseMessage> handleInvalidCustomerException(InvalidCustomerException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),null,  HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ResponseMessage> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Get the message from the exception
        String errorMessage = ex.getMostSpecificCause().getMessage();

        // Attempt to split and trim the error message to extract specific details
        String message = processErrorMessage(errorMessage);

        ErrorResponse errorResponse = new ErrorResponse(message,null,  HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseMessage> handleException(Exception ex) {
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String message = ex.getMessage();
        if(ex instanceof NoResourceFoundException  ){
            message = processErrorMessage(ex.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;
        }else if(ex instanceof HttpRequestMethodNotSupportedException){
            message = processErrorMessage(ex.getMessage());
            httpStatus = HttpStatus.BAD_REQUEST;

        }
        ErrorResponse errorResponse = new ErrorResponse(message,null, httpStatus);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), httpStatus);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ResponseMessage> handleInvalidPathVariable(MethodArgumentTypeMismatchException ex) {
        String errorMessage = "Invalid input: '" + ex.getValue() + "' is not a valid ID.";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage,null,  HttpStatus.BAD_REQUEST);
        // Return a custom message or response
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseMessage> handleRuntimeException(RuntimeException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(),null,  HttpStatus.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // This method will handle MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseMessage> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex) {

        // Get the list of validation errors
        BindingResult bindingResult = ex.getBindingResult();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();

        // Map the FieldErrors to a list of error messages
        List<String> errorMessages = fieldErrors.stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());

        // Create and return a response containing the error messages
        ErrorResponse errorResponse = new ErrorResponse("Validation failed : "+errorMessages,null,  HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(ResponseMessage.generateResponse(errorResponse), HttpStatus.BAD_REQUEST);
    }
    private String processErrorMessage(String errorMessage) {
        if (errorMessage == null || errorMessage.isEmpty()) {
            return "Unknown database error occurred.";
        }

        // Example of trimming and splitting to extract meaningful details (customize according to the DB error pattern)
        // This pattern may vary depending on the database used (PostgreSQL, MySQL, etc.).
        String[] parts = errorMessage.split("DETAIL:"); // Example of splitting based on a pattern

        // Check if there is a specific detail in the message
        if (parts.length > 1) {
            // Extract and trim the detail message
            return parts[1].trim();
        }

        // Default message if no specific detail is found
        return errorMessage.split(":")[0].trim();
    }


}
