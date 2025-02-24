package com.example.myapp.exception.custom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class BaseResponse {
    private String message;
    private String responseCode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:MM:ss")
    private String timestamp;
    private String status;
    private Object data; // This can be any object returned from the operation, e.g., the created product
    private String requestId; // Unique request identifier
    private String path; // The path of the API that was accessed
    private String environment;
}
