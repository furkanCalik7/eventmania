package com.database.eventmania.backend.entity;

import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class User extends Account {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    @Transient
    private Integer age;
    private LocalDate dob;

    public User(Long accountId, String email, String password,
                String firstName, String lastName, Gender gender,
                String phoneNumber, LocalDate dob) {
        super(accountId, email, password);
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

