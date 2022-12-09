package com.database.eventmania.backend.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.database.eventmania.backend.entity.Admin;
import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.entity.Organization;
import com.database.eventmania.backend.repository.AdminRepository;
import com.database.eventmania.backend.repository.OrganizationRepository;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountService {
    private final UserRepository userRepository;
    private final AdminRepository adminRepository;
    private final OrganizationRepository organizationRepository;
    private final AuthenticationService authenticationService;
    @Autowired
    public AccountService(UserRepository userRepository, AdminRepository adminRepository,
                          OrganizationRepository organizationRepository, AuthenticationService authenticationService) {
        this.userRepository = userRepository;
        this.adminRepository = adminRepository;
        this.organizationRepository = organizationRepository;
        this.authenticationService = authenticationService;
    }

    public Map<String, String> findAccountByEmailAndPassword(String email, String password) throws SQLException {
        String hashedPassword = PasswordService.encrypt(password);
        Algorithm algorithm = Algorithm.HMAC256(authenticationService.getKey());
        Map<String, String> returnMap = new HashMap<>();

        BasicUser user = userRepository.getUserByEmailAndPassword(email, hashedPassword);
        if (user != null) {
            String token = JWT.create()
                    .withIssuer("eventmania")
                    .withClaim("roleID", "BasicUser")
                    .withClaim("userID", user.getAccountId())
                    .sign(algorithm);
            returnMap.put("token", token);
            returnMap.put("type", "BasicUser");

            return returnMap;
        }

        Organization organization = organizationRepository.getOrganizationByEmailAndPassword(email, hashedPassword);
        if (organization != null) {
            String token = JWT.create()
                    .withIssuer("eventmania")
                    .withClaim("roleID", "Organization")
                    .withClaim("userID", organization.getAccountId())
                    .sign(algorithm);
            returnMap.put("token", token);
            returnMap.put("type", "Organization");

            return returnMap;
        }

        Admin admin = adminRepository.getAdminByEmailAndPassword(email, hashedPassword);
        if (admin != null) {
            String token = JWT.create()
                    .withIssuer("eventmania")
                    .withClaim("roleID", "Admin")
                    .withClaim("userID", admin.getAccountId())
                    .sign(algorithm);
            returnMap.put("token", token);
            returnMap.put("type", "Admin");

            return returnMap;
        }

        return null;

    }
}
