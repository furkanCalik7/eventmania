package com.database.eventmania.backend.service;

import com.database.eventmania.backend.repository.CardRepository;
import com.database.eventmania.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class CardService {
    private final CardRepository cardRepository;
    @Autowired
    public CardService(CardRepository cardRepository) {
        this.cardRepository = cardRepository;
    }

    public boolean createCard(String cardNumber, String cardOwner, String cardTitle, Integer expirationMonth, Integer expirationYear) throws SQLException {
        return cardRepository.createCard(cardNumber, cardOwner, cardTitle, expirationMonth, expirationYear);
    }

    public boolean deleteCard(String cardNumber) throws SQLException{
        return cardRepository.deleteCard(cardNumber);
    }

    public boolean updateCard(String cardNumber, String cardOwner, String cardTitle, Integer expirationMonth, Integer expirationYear) throws SQLException {
        return cardRepository.updateCard(cardNumber, cardOwner, cardTitle, expirationMonth, expirationYear);
    }
}
