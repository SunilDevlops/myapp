package com.example.myapp.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseHeader {
    private String requestId;
    private String status;
    private String responseCode;
    private String timestamp;
    private String path;
    private String environment;
}
