package com.database.eventmania.backend.service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthenticationService {
    private UserRepository userRepository;
    private final String key = "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣀⣀⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣾⣿⣿⣿⣿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⣿⣿⣿⣿⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⣿⣿⣿⣿⣿⡿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣤⣶⣷⣶⣿⣿⣿⣿⣿⠟⠋⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⣿⣿⣿⣿⣿⣏⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣶⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡷⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⢸⣛⠛⢹⣿⣿⣿⣿⡿⠏⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡿⠟⠛⠋⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⢀⣀⣀⣠⣾⣯⣭⣙⣾⣿⣿⣿⣿⣴⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⢀⣾⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⢠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠿⢿⡿⠉⠻⢿⣷⣦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠙⠛⠻⠿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡟⠉⢹⠏⣿⣿⣿⣿⣿⡄⠀⠀⠀⠀⠀⠉⠻⣿⣷⣤⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⣿⣿⣿⣿⠏⢸⣿⣿⣿⣿⣿⠀⠀⣼⠀⢸⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠙⠻⣿⣶⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠈⣿⣿⣿⣿⣿⣿⣿⡟⢀⣾⣿⣿⣿⡏⢹⠀⠀⡟⠀⠸⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠙⠿⣿⣦⣄⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⢹⣿⣿⣿⣿⣿⣿⣧⣾⣿⣿⣿⣿⣧⠘⡆⣸⠃⠀⣤⣹⣿⣿⣿⣇⣤⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⢿⣷⣦⣀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠈⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠻⣧⡉⠁⠀⠀⣿⢿⣿⣿⣿⣿⣿⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠛⢿⣷⣤⡀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠈⠻⠛⠋⢠⣿⣿⣿⣿⡿⠁⠉⠀⠹⣷⡀⠀⠀⠉⢸⣿⣿⣿⣿⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⢿⡄" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢠⣿⣿⣿⣿⣿⠃⠀⠀⠀⠀⠹⣷⡀⠀⠀⢸⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣼⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠙⣷⡀⠀⠘⣿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠘⣷⠀⠀⢿⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⠃⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠘⠇⠀⢸⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣯⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣽⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣦⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣾⣿⣿⣿⣦⡄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀" +
            "⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠛⠛⠛⠉⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⠛⠛⠉⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀";

    private AuthenticationService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public Map<String, Object> authenticateAccount(String email, String password) {
//        // Pasword service lazim
//        String hashedPassword = PasswordService.encrypt(password);
//
//        Optional<User> userOptional = userRepository.findUserByEmailAndPassword(email, hashedPassword);
//        Map<String, Object> returnMap = new HashMap<>();
//        if(userOptional.isPresent()) {
//            Algorithm algorithm = Algorithm.HMAC256(key);
//            returnMap.put("userID", userOptional.get().getId());
//            if(userOptional.get() instanceof Admin) {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.ADMIN_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "admin");
//            } else if(userOptional.get() instanceof Student) {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.STUDENT_USER_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "student");
//            } else if(userOptional.get() instanceof Alumni) {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.ALUMNI_USER_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "alumni");
//            } else if(userOptional.get() instanceof Guest) {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.GUEST_USER_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "guest");
//            }else if(userOptional.get() instanceof Trainer) {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.TRAINER_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "trainer");
//            } else if(userOptional.get() instanceof BasicUser) {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.BASIC_USER_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "basic_user");
//            } else {
//                String token = JWT.create()
//                        .withIssuer("bilgym")
//                        .withClaim("roleID", UserRolesConstraint.GYM_STAFF_TYPE)
//                        .withClaim("userID", userOptional.get().getId())
//                        .sign(algorithm);
//                returnMap.put("token", token);
//                returnMap.put("type", "gym_staff");
//            }
//            return returnMap;
//        }
//        returnMap.put("token", null);
//        return returnMap;
//    }
}