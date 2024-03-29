package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.*;
import com.database.eventmania.backend.entity.enums.Gender;
import com.database.eventmania.backend.entity.enums.VerificationStatus;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseRepository {
    private String SERVER_URL = "jdbc:postgresql://db-postgresql-fra1-49552-do-user-8422201-0.b.db.ondigitalocean.com:25060/defaultdb";
    private String USERNAME = "doadmin";

    private String PASSWORD = "AVNS_FBj67GgeDeXYsD6RRIG";

    private Connection connection;


    public BaseRepository() {

    }

    public void connect() {
        try {
            this.connection = DriverManager.getConnection(SERVER_URL, USERNAME, PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Connection getConnection() {
        return this.connection;
    }

    public BasicUser convertQueryResultToUser(ResultSet rs) throws SQLException {
        Gender gender;
        String databaseGender = "male";
        if (databaseGender.equals("male")) {
            gender = Gender.MALE;
        } else if (databaseGender.equals("female")) {
            gender = Gender.FEMALE;
        } else {
            gender = Gender.OTHER;
        }

        return new BasicUser(
                rs.getLong("user_id"),
                rs.getLong("wallet_id"),
                rs.getString("email"),
                rs.getString("hash_password"),
                rs.getString("first_name"),
                rs.getString("last_name"),
                gender,
                rs.getString("phone_number"),
                rs.getDate("date_of_birth").toLocalDate()
        );
    }

    public Admin convertQueryResultToAdmin(ResultSet rs) throws SQLException {
        // accountId, email, password
        return new Admin(
                rs.getLong("account_id"),
                rs.getString("email"),
                rs.getString("password")
        );
    }

    public Organization convertQueryResultToOrganization(ResultSet rs) throws SQLException {
        String databaseVerificationStatus = rs.getString("verification_status").toLowerCase();
        VerificationStatus verificationStatus;
        if (databaseVerificationStatus.equals("confirmed")) {
            verificationStatus = VerificationStatus.CONFIRMED;
        } else if (databaseVerificationStatus.equals("rejected")) {
            verificationStatus = VerificationStatus.REJECTED;
        } else {
            verificationStatus = VerificationStatus.UNDER_REVIEW;
        }
        return new Organization(
                rs.getLong("organization_id"),
                rs.getString("email"),
                rs.getString("hash_password"),
                rs.getLong("admin_id"),
                rs.getLong("wallet_id"),
                rs.getString("organization_name"),
                rs.getString("description"),
                rs.getString("phone_number"),
                rs.getDate("verification_date").toLocalDate(),
                verificationStatus,
                rs.getString("feedback")
        );
    }

    public Rating convertQueryResultToRating(ResultSet rs) throws SQLException {
        return new Rating(
                rs.getLong("rating_id"),
                rs.getLong("event_id"),
                rs.getLong("user_id"),
                rs.getInt("point"),
                rs.getString("topic"),
                rs.getString("comment")
        );
    }

    public Wallet convertQueryResultToWallet(ResultSet rs) throws SQLException{
        return new Wallet(
                Long.valueOf(rs.getInt("wallet_id")),
                rs.getInt("balance")
        );
    }

}

