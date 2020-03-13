package com.bungeeinc.bungeeapp.api.service.jwtconfig;

import com.bungeeinc.bungeeapp.api.service.UserService;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    private final UserService userService;

    public JwtUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {

        User user = userService.getByUsername(s);

        if(user == null) {
            throw new UsernameNotFoundException(s);
        } else {
            return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.emptyList());
        }
/*        Optional<User> user = Optional.ofNullable(userService.getByUsername(s));

        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + s));
        return user.map(BungeeUserDetails::new).get();*/


        //return user.map(User::new).get();
        /*BungeeUserDetails user = userService.getUserDetailsByUsername(s);
        if(user == null) {
            throw new UsernameNotFoundException(s);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Collections.EMPTY_LIST);*/
    }
}
