package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.service.UserService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.request.FollowRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/auth")
    public LoginResponse auth(@RequestBody @Valid LoginRequest request) {
        return userService.auth(request);
    }

    // TODO:
    @PostMapping("/follow")
    public FollowResponse follow(@RequestBody @Valid FollowRequest request) {
        return userService.follow(request);
    }


}
