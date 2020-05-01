package com.bungeeinc.bungeeapp.api.service.jwtconfig;

import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final DatabaseService databaseService;

    public JwtUserDetailsService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        BungeeUserDetails user = databaseService.getAccountDao().getByUsername(s);

        if(user == null) {
            throw new UsernameNotFoundException(s);
        } else {
            return user;
        }
    }
}
