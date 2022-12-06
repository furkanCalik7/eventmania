package com.database.eventmania.backend.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseRepository {

    final String SERVER_URL = "";
    final String USERNAME = "";
    final String PASSWORD = "";

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
}
