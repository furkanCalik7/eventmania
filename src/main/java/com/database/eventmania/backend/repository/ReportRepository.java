package com.database.eventmania.backend.repository;

import com.database.eventmania.backend.entity.BasicUser;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

@Repository
public class ReportRepository extends BaseRepository {
    public ReportRepository() {
        super.connect();
    }
    public ArrayList<BasicUser> getMostActiveUsers() throws SQLException {
        Connection conn = super.getConnection();
        if (conn == null) {
            throw new SQLException("Connection is null");
        }
        String query = "SELECT U.user_id, U.email, U.first_name, U.last_name, " +
                "U.phone_number, count(*) as count " +
                "FROM event E NATURAL JOIN join_event NATURAL JOIN basicuser U " +
                "GROUP BY U.user_id, U.email, U.first_name, U.last_name " +
                "ORDER BY count DESC";
        PreparedStatement statement = conn.prepareStatement(query);
        ResultSet rs = statement.executeQuery();
        ArrayList<BasicUser> users = new ArrayList<>();
        while(rs.next()){
            BasicUser user = new BasicUser();
            user.setAccountId(rs.getLong("user_id"));
            user.setEmail(rs.getString("email"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setPhoneNumber(rs.getString("phone_number"));
            user.setAge(rs.getInt("count"));
            users.add(user);
        }
        return users;
    }
}
