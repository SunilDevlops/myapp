package com.example.myapp.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data // Generates getters, setters, toString(), equals(), and hashCode() methods automatically
@NoArgsConstructor // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all arguments
@Builder // Adds a builder pattern for easy object creation
@Table(name = "CUSTOMER_T")

public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  // PK - UUID
    @Version
    private Long version;
    @Column(name= "FIRST_NAME")
    private String firstName;
    @Column(name= "MIDDLE_NAME")
    private String middleName;  // Can be nullable
    @Column(name= "LAST_NAME")
    private String lastName;

    @Column(name="EMAIL_ADDRESS", nullable = false, unique = true)
    private String emailAddress; // Unique

    @Embedded
    private PhoneNumber phoneNumber;


}
