package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Rating;
import com.database.eventmania.backend.entity.Wallet;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public Wallet getWalletById(Integer walletId) throws SQLException{
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "SELECT * FROM Wallet WHERE wallet_id =?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, walletId);
            ResultSet rs = stmt.executeQuery();

            if(rs.next())
                return convertQueryResultToWallet(rs);
            return null;
        }
        throw new SQLException("Connection to the database failed");

    }
    public boolean deleteWallet(Integer walletId) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "DELETE FROM Wallet WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, walletId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean updateWallet(Integer walletId, Integer balance) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, balance);
            stmt.setInt(2, walletId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }

    public boolean addMoney(Integer walletId, Integer amount) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = balance + ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, walletId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean withdrawMoney(Integer walletId, Integer amount) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = balance - ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, walletId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
    public boolean buyTicket(Integer buyerWalletId, Integer sellerWalletId, Integer amount) throws SQLException {
        Connection conn = super.getConnection();
        if(conn != null){
            String query = "UPDATE Wallet " +
                            "SET balance = balance - ? " +
                            "WHERE wallet_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, buyerWalletId);
            stmt.executeUpdate();
            query = "UPDATE Wallet " +
                    "SET balance = balance + ? " +
                    "WHERE wallet_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, amount);
            stmt.setInt(2, sellerWalletId);
            stmt.executeUpdate();
            return true;
        }
        return false;
    }
}
