package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.entity.enums.Gender;
import com.database.eventmania.backend.model.EventModel;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public BasicUser getUserById(Long userId) throws SQLException {
        return userRepository.getUserById(userId);
    }

    public BasicUser getUserByEmailAndPassword(String email, String password) throws SQLException {
        return userRepository.getUserByEmailAndPassword(email, password);
    }

    public BasicUser getUserByEmail(String email) throws SQLException {
        return userRepository.getUserByEmail(email);
    }

    public boolean saveUser(String email, String password,
                            String firstName, String lastName, String gender,
                            String phoneNumber, String dob) throws SQLException {
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        Gender genderEnum = Gender.valueOf(gender.toUpperCase());
        return userRepository.saveUser(email, hashedPassword, firstName, lastName, genderEnum, phoneNumber, LocalDate.parse(dob));
    }

    public ArrayList<BasicUser> getALlUsers() throws SQLException {
        return userRepository.getAllUsers();
    }

    public boolean deleteUserById(Long userId) throws SQLException {
        return userRepository.deleteUserById(userId);
    }

    public ArrayList<EventModel> listJoinedEvents(String userEmail) throws SQLException {
        return userRepository.listJoinedEvents(userEmail);
    }

    public ArrayList<EventModel> listFutureEvents(String userEmail) throws SQLException {
        return userRepository.listFutureEvents(userEmail);
    }

    public boolean joinUnticketedEvent(String userEmail, String eventId) throws SQLException {
        return userRepository.joinUnticketedEvent(Long.valueOf(eventId), userEmail);
    }

    public ArrayList<EventModel> getOrganizedEvents(String userEmail) throws SQLException {
        return userRepository.getOrganizedEvents(userEmail);
    }

    public boolean isUserInEvent(String eventId, String email) throws SQLException {
        return userRepository.isUserInEvent(Long.valueOf(eventId), email);
    }

    public String getEventState(String eventId) throws SQLException {
        return userRepository.getEventState(Long.valueOf(eventId));
    }

    public boolean withdrawFromEvent(String eventId, String email) throws SQLException {
        return userRepository.withdrawFromEvent(Long.valueOf(eventId), email);
    }
}