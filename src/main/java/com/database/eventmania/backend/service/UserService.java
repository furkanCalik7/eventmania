package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

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

    public boolean saveUser(String username, String password) throws SQLException {
        String hashedPassword = bCryptPasswordEncoder.encode(password);
        return userRepository.saveUser(username, hashedPassword);
    }
}
