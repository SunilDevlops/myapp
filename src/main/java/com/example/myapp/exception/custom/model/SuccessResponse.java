package com.example.myapp.exception.custom.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
public class SuccessResponse extends BaseResponse {

    public SuccessResponse(String message, Object data, HttpStatus httpStatus) {
        this.setStatus("Success");
        this.setResponseCode(httpStatus.toString());
        this.setTimestamp(new java.util.Date().toString());
        this.setMessage(message);
        this.setData(data);
        this.setRequestId(MDC.get("requestId")); // Retrieved from MDC
        this.setPath(MDC.get("path")); // Retrieved from MDC
        this.setEnvironment(MDC.get("environment")); // Retrieved from MDC
    }
}
