package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.Gender;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
public class BasicUser extends Account {
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
        super(userId, email, hashPassword);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
    }

    public BasicUser(Long accountId, String email, String hashPassword, String firstName, String lastName, Gender gender, String phoneNumber, Integer age) {
        super(accountId, email, hashPassword);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.age = age;
    }

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

}

