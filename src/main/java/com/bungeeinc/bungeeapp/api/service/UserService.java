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
    public UserService(DatabaseService databaseService, HttpServletRequest servletRequest) {
        this.databaseService = databaseService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.tokenUtil = new JwtTokenUtil();
    }

    /**
     *
     * @param request LoginRequest
     * @return login response with a token if success
     */
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

    /**
     *
     * @param req HttpServletRequest
     * @param request FollowRequest
     * @return follow type response
     */
    public FollowResponse follow(FollowRequest request, HttpServletRequest req) {
        String jwtToken = req.getHeader("Authorization").substring(7);
        String username = tokenUtil.getUsernameFromToken(jwtToken);
        User user = databaseService.getUserDao().findByUsername(username);
        System.out.println("HttpServletRequest token: " + req.getHeader("Authorization"));
        System.out.println("HttpServletRequest auth type: " + req.getAuthType());


        if (databaseService.getUserDao().isFollow(user.getId(), request.getFollowingUserId())) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        if(!tokenUtil.validateToken(jwtToken, user)) {
            return new FollowResponse(FollowResponseType.FAILED);
        } else {
            databaseService.getUserDao().follow(user.getId(), request.getFollowingUserId());
            return new FollowResponse(FollowResponseType.SUCCESS);
        }
    }

    /**
     *
     * @param request RegisterRequest
     * @return RegisterResponse
     */
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
