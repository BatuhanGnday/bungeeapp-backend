package com.bungeeinc.bungeeapp.database.models.account;

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
public class BungeeUserDetails implements UserDetails {


    private int id;

    @NonNull
    private String username;

    @NonNull
    private String password;

    private boolean isDeleted;

    private boolean isEnabled;

    private Timestamp createdOn;

    public BungeeUserDetails(String username, String password) {
        this.username = username;
        this.password = password;
        this.isDeleted = false;
        this.isEnabled = true;
    }

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

    public static class Mapper implements ColumnMapper<BungeeUserDetails> {

        @Override
        public BungeeUserDetails map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
            int id = r.getInt("id");
            String username = r.getString("username");
            String password = r.getString("password");
            boolean isDeleted = r.getBoolean("deleted");
            boolean isEnabled = r.getBoolean("enabled");
            Timestamp createdOn = r.getTimestamp("created_on");
            BungeeUserDetails user = new BungeeUserDetails(username, password);
            user.setId(id);
            user.setCreatedOn(createdOn);
            user.setEnabled(isEnabled);
            user.setDeleted(isDeleted);
            return user;
        }
    }

}
