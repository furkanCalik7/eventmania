package com.database.eventmania.backend.model;

import com.database.eventmania.backend.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
public class BasicUserRegisterModel {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    private LocalDate dob;
}
