package com.database.eventmania.backend.entity;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class BasicUser {
    private Long userId;
    private String email;
    private String hashPassword;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    @Transient
    private Integer age;
    private LocalDate dob;

    public BasicUser(Long userId, String email, String hashPassword,
                String firstName, String lastName, Gender gender,
                String phoneNumber, LocalDate dob) {
        this.userId = userId;
        this.email = email;
        this.hashPassword = hashPassword;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

}

