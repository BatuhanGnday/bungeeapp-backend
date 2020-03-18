package com.bungeeinc.bungeeapp.database.models.user;

import lombok.Data;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;

@Data
public class User implements UserDetails {


    private int id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private String role;

    private String biography;

    @NonNull
    private boolean isPrivate = false;

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.EMPTY_LIST;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    public static class Mapper implements ColumnMapper<User> {

        @Override
        public User map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
            int id = r.getInt("id");
            String username = r.getString("username");
            String password = r.getString("password");
            String role = r.getString("role");
            String firstName = r.getString("first_name");
            String lastName = r.getString("last_name");
            String email = r.getString("email");
            String biography = r.getString("biography");
            int age = r.getInt("age");
            boolean isDeleted = r.getBoolean("is_deleted");
            String imageKey = r.getString("image_key");
            Timestamp createdOn = r.getTimestamp("created_on");
            boolean isPrivate = r.getBoolean("private");
            User user = new User(username, password, firstName, lastName, email);
            user.setId(id);
            user.setRole(role);
            user.setCreatedOn(createdOn);
            user.setBiography(biography);
            user.setImageKey(imageKey);
            user.setAge(age);
            user.setDeleted(isDeleted);
            user.setPrivate(isPrivate);
            return user;
        }
    }

}
