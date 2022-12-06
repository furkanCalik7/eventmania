package com.database.eventmania.backend.entity;

import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Period;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User extends Account {
    private String firstName;
    private String lastName;
    private Gender gender;
    private String phoneNumber;
    @Transient
    private Integer age;
    private LocalDate dob;

    public Integer getAge() {
        return Period.between(this.dob, LocalDate.now()).getYears();
    }

}

enum Gender {
    MALE,
    FEMALE,
    OTHER
}