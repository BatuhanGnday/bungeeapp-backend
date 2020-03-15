package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.request.FollowRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserService {

    private final DatabaseService databaseService;
    private JwtTokenUtil tokenUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private HttpServletRequest servletRequest;

    @Autowired
    public UserService(DatabaseService databaseService, HttpServletRequest servletRequest) {
        this.databaseService = databaseService;
        this.servletRequest = servletRequest;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.tokenUtil = new JwtTokenUtil();
    }

        public LoginResponse auth(LoginRequest request) {
            User user = databaseService.getUserDao().findByUsername(request.getUsername());

            if (user == null) {
                return new LoginResponse(LoginResponseType.USER_NOT_EXIST, null);
            }
            if(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())){

                    return new LoginResponse(LoginResponseType.SUCCESS, tokenUtil.generateToken(user));
            }
            return new LoginResponse(LoginResponseType.PASSWORD_FAIL,null);
        }

    public FollowResponse follow(String token, FollowRequest request) {

        String jwtToken = token.substring(7);
        String username = tokenUtil.getUsernameFromToken(jwtToken);
        User user = databaseService.getUserDao().findByUsername(username);

        if (databaseService.getUserDao().isFollow(user.getId(), request.getFollowingUserId())) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        if(!tokenUtil.validateToken(jwtToken, user)) {
            return new FollowResponse(FollowResponseType.FAILED);
        } else {
            databaseService.getUserDao().follow(user.getId(), request.getFollowingUserId());
            return new FollowResponse(FollowResponseType.SUCCESS);
        }
        /*User user = databaseService.getUserDao().getById(request.getUserId());
        String token = tokenUtil.generateToken(user);

        if(!databaseService.getUserDao().isFollow(request.getUserId(), request.getFollowingUserId())
                && tokenUtil.validateToken(token, user)){
            return new FollowResponse(FollowResponseType.SUCCESS);
        }
        databaseService.getUserDao().follow(request.getUserId(),request.getFollowingUserId());
        return new FollowResponse(FollowResponseType.SUCCESS);*/
    }

    public RegisterResponse register(RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                bCryptPasswordEncoder.encode(request.getPassword()),
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
        return this.databaseService.getUserDao().findByUsername(username);
    }


    public User getById(int id) {
        return this.databaseService.getUserDao().getById(id);
    }

}
