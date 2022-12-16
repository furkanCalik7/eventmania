package com.database.eventmania.backend.service;

import com.database.eventmania.backend.repository.AccountRepository;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final AccountRepository accountRepository;

    public UserDetailsServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            HashMap<String, String> account = accountRepository.getAccountByEmail(email);
            if (account == null) throw new UsernameNotFoundException("Username or password is invalid.");
            return new User(account.get("email"), account.get("password"), List.of(new SimpleGrantedAuthority(account.get("type"))));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
