package com.database.eventmania.backend.entity;

import com.database.eventmania.backend.entity.enums.VerificationStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Organization extends Account {
    private Long adminId;
    private Long walletId;
    private String organizationName;
    private String description;
    private String phoneNumber;
    private LocalDate verificationDate;
    private VerificationStatus verificationStatus;
    private String feedback;

    public Organization(Long organizationId, String email, String hashPassword, Long adminId, Long walletId,
                        String organizationName, String description, String phoneNumber, LocalDate verificationDate,
                        VerificationStatus verificationStatus, String feedback) {
        super(organizationId, email, hashPassword);
        this.adminId = adminId;
        this.walletId = walletId;
        this.organizationName = organizationName;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.verificationDate = verificationDate;
        this.verificationStatus = verificationStatus;
        this.feedback = feedback;
    }
}
