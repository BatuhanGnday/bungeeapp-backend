package com.bungeeinc.bungeeapp.database.models;

import lombok.Data;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Data
public class User {


    private int id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;

    private int age;

    private boolean isDeleted;

    private String imageKey;

    private Timestamp createdOn;

    public static class Mapper implements ColumnMapper<User> {

        @Override
        public User map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
            int id = r.getInt("id");
            String username = r.getString("username");
            String password = r.getString("password");
            String firstName = r.getString("first_name");
            String lastName = r.getString("last_name");
            String email = r.getString("email");
            int age = r.getInt("age");
            boolean isDeleted = r.getBoolean("is_deleted");
            String imageKey = r.getString("image_key");
            Timestamp createdOn = r.getTimestamp("created_on");
            User user = new User(username, password, firstName, lastName, email);
            user.setId(id);
            user.setCreatedOn(createdOn);
            return user;
        }
    }

}
