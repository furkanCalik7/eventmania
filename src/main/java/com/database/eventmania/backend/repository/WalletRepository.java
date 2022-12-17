package com.database.eventmania.backend.repository;

import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Repository
public class WalletRepository extends BaseRepository {
    public WalletRepository(){
        super.connect();
    }
    public boolean createWallet(Integer balance) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "INSERT INTO Wallet (balance) VALUES (?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, balance);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean deleteWallet(Long walletId) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "DELETE FROM Wallet WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, walletId.intValue());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean updateWallet(Long walletId, Integer balance) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, balance);
            stmt.setInt(2, walletId.intValue());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    public boolean addMoney(Long walletId, Integer amount) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = balance + ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, walletId.intValue());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean withdrawMoney(Long walletId, Integer amount) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = balance - ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, walletId.intValue());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean buyTicket(Long buyerWalletId, Long sellerWalletId, Integer amount) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = balance - ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, buyerWalletId.intValue());
            stmt.executeUpdate();
            query = "UPDATE Wallet " +
                    "SET balance = balance + ? " +
                    "WHERE wallet_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, sellerWalletId.intValue());
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
}
