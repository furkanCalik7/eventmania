package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.BasicUser;
import com.database.eventmania.backend.entity.enums.Gender;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Repository
public class UserRepository extends BaseRepository {

    public UserRepository() {
        super.connect();
    }

    // TODO: connect to database and test the code
    public BasicUser getUserById(Long userId) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM BasicUser WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setLong(1, userId);

            ResultSet rs = stmt.executeQuery();

            if (rs.next())
                return convertQueryResultToUser(rs);
            return null;
        }

        throw new SQLException("Connection to the database failed");
    }

    public BasicUser getUserByEmailAndPassword(String email, String hashedPassword) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM BasicUser WHERE email = ? AND hash_password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);

            stmt.setString(1, email);
            stmt.setString(2, hashedPassword);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return convertQueryResultToUser(rs);
            return null;
        }

        throw new SQLException("Connection to the database failed");
    }

    public BasicUser getUserByEmail(String email) throws SQLException {
        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "SELECT * FROM BasicUser WHERE email = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, email);

            ResultSet rs = stmt.executeQuery();
            if (rs.next())
                return convertQueryResultToUser(rs);
            return null;
        }
        throw new SQLException("Connection to the database failed");
    }

    public boolean saveUser(String email, String hashPassword,
                            String firstName, String lastName, Gender gender,
                            String phoneNumber, LocalDate dob) throws SQLException {

        Connection conn = super.getConnection();

        if (conn != null) {
            String query = "INSERT INTO BasicUser (hash_password, email, first_name, last_name, gender, phone_number, date_of_birth) " +
                    "VALUES (?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, hashPassword);
            stmt.setString(2, email);
            stmt.setString(3, firstName);
            stmt.setString(4, lastName);
            stmt.setString(5, gender.name());
            stmt.setString(6, phoneNumber);
            // TODO: I fixed the date time problem in the frontend, this should be changed I guess.
            stmt.setDate(7, java.sql.Date.valueOf(dob));

            ResultSet rs = stmt.executeQuery();

            return true;
        }
        return false;
    }
}
