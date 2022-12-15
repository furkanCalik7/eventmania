package com.database.eventmania.backend.service;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            // TODO: add the view implementation that I can get the type of the user and the username at the same query
            // RECOMMENDATION: Use the view query in the homework
            // TODO: make the three table (user, admin and orginaztion) sequential ids increment
            BasicUser user = userRepository.getUserByEmail(username);
            if (user == null) throw new UsernameNotFoundException("Username or password is invalid.");
            return new User(user.getEmail(), user.getHashPassword(), List.of(new SimpleGrantedAuthority("BasicUser")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
