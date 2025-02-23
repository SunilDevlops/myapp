package com.example.myapp.response;

import com.example.myapp.exception.custom.model.BaseResponse;
import com.example.myapp.exception.custom.model.ErrorResponse;
import com.example.myapp.exception.custom.model.SuccessResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseMessage {
    private ResponseHeader responseHeader;
    private ResponseData responseData;

        // Helper method to generate the response
/*    public ResponseMessage generateResponse(Object<T> message) {
        SuccessResponse successResponse = new SuccessResponse(
                "Success",
                "200 OK",
                new java.util.Date().toString(),
                HttpStatus.NO_CONTENT.toString(),
                message
        );
        // Create the ResponseHeader
        ResponseHeader responseHeader = new ResponseHeader(
                "Success", // Status
                UUID.randomUUID().toString(), // Request ID (could be a generated UUID or trace ID)
                "200 OK", // Response Code
                new java.util.Date().toString(),
                "/api/customers/" + customerId, // Path
                "PROD" // Environment (you can dynamically fetch this based on profiles)
        );

        // Create the ResponseData (the success message)
        ResponseData responseData = new ResponseData(message);

        // Create and return the ResponseMessage with both header and data
        return new ResponseMessage(responseHeader, responseData);
    }*/

    public static ResponseMessage  generateResponse(BaseResponse response) {
        ResponseHeader responseHeader = new ResponseHeader(
                response.getRequestId(),
                response.getStatus(), // Status
                response.getResponseCode(), // Request ID
                response.getTimestamp(), // Error Code
                response.getPath(), // Path
                response.getEnvironment() // Environment
        );

        ResponseData responseData = new ResponseData(response.getMessage(), response.getData());

        return new ResponseMessage(responseHeader, responseData);
    }
}
