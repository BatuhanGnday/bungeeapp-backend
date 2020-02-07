package com.bungeeinc.bungeeapp.api.service.jwtconfig;

import com.bungeeinc.bungeeapp.api.service.UserService;
import com.bungeeinc.bungeeapp.database.models.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userService.getByUsername(s);
        if(user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.EMPTY_LIST);
    }
}
