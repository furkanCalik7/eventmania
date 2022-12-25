package com.database.eventmania.backend.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
@Repository
public class CardRepository extends BaseRepository{

    public CardRepository() {
        super.connect();
    }

    public boolean createCard(String cardNumber, String cardOwner, String cardTitle, Integer expirationMonth, Integer expirationYear) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "INSERT INTO Card (card_number, card_owner, card_title, expiration_month, expiration_year) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardNumber);
            stmt.setString(2, cardOwner);
            stmt.setString(3, cardTitle);
            stmt.setString(4, expirationMonth.toString());
            stmt.setString(5, expirationYear.toString());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    /*
    Delete a card with the given card number
     */
    public boolean deleteCard( String cardNumber) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "DELETE FROM Card WHERE card_number = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardNumber);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }


    public boolean updateCard(String cardNumber, String cardOwner , String cardTitle, Integer expirationMonth, Integer expirationYear) throws SQLException{
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Card " +
                            "SET cardNumber = ? ," +
                            "cardOwner = ? ," +
                            "cardTitle = ? ," +
                            "expirationMonth = ? ," +
                            "expirationDate = ? " +
                            "WHERE cardNumber = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, cardNumber);
            stmt.setString(2, cardOwner);
            stmt.setString(3, cardTitle);
            stmt.setString(4, expirationMonth.toString());
            stmt.setString(5, expirationYear.toString());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
}
