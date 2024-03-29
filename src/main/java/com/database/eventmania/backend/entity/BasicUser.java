package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.Gender;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
public class BasicUser extends Account {
    private Long walletId;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    
    private Integer age;
    private LocalDate dob;

    public BasicUser(Long userId, Long walletId, String email, String hashPassword,
                     String firstName, String lastName, Gender gender,
                     String phoneNumber, LocalDate dob) {
        super(userId, email, hashPassword);
        this.walletId = walletId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
    }
}

