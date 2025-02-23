package com.example.myapp.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data // Generates getters, setters, toString(), equals(), and hashCode() methods automatically
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all arguments
@Builder
public class PhoneNumber {
    @Column(name= "COUNTRY_CODE")
    private String countryCode;
    @Column(name= "AREA_CODE")
    private String areaCode;
    @Column(name= "prefix")
    private String prefix;
    @Column(name= "LINE_NUMBER")
    private String lineNumber;
}
