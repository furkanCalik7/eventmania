package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.Gender;
import com.database.eventmania.backend.entity.User;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseRepository {
    @Value("${spring.datasource.url}")
    private String SERVER_URL;
    @Value("${spring.datasource.username}")
    private String USERNAME;
    @Value("${spring.datasource.password}")
    private String PASSWORD;

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

    // TODO: should we check if gender is null? (consider adding Gender.null)
    public User convertQueryResultToUser(ResultSet rs) throws SQLException {
        // accountId, email, password, firstName, lastName, gender, phoneNumber, dob

        Gender gender;

        String databaseGender = rs.getString("gender").toLowerCase();

        if(databaseGender.equals("male")) {
            gender = Gender.MALE;
        }
        else if(databaseGender.equals("female")) {
            gender = Gender.FEMALE;
        }
        else {
            gender = Gender.OTHER;
        }

        return new User(
                rs.getLong("accountId"),
                rs.getString("email"),
                rs.getString("password"),
                rs.getString("firstName"),
                rs.getString("lastName"),
                gender,
                rs.getString("phoneNumber"),
                rs.getDate("dob").toLocalDate()
        );
    }
}
