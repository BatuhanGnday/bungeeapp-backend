package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final DatabaseService databaseService;
    private JwtTokenUtil tokenUtil;

    @Autowired
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.tokenUtil = new JwtTokenUtil();
    }

    public LoginResponse auth(LoginRequest request) {
        User user = databaseService.getUserDao().getByUsername(request.getUsername());

        if(request.getPassword().equals(user.getPassword())){
            return new LoginResponse(LoginResponseType.SUCCESS, tokenUtil.generateToken(user));
        }
        return new LoginResponse(LoginResponseType.PASSWORD_FAIL,null);
    }
    public RegisterResponse register(RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );
        if (this.databaseService.getUserDao().isExistByUsernameOrEmail(user)) {
            return new RegisterResponse(RegisterResponseType.USERNAME_OR_EMAIL_EXISTS);
        }
        user.setId(
                this.databaseService.getUserDao().createUser(user)
        );
        return new RegisterResponse(RegisterResponseType.SUCCESS);
    }

    public User getByUsername(String username) {
        return this.databaseService.getUserDao().getByUsername(username);
    }
}
