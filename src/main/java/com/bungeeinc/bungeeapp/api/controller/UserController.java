package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.service.UserService;
import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.users.login.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.users.login.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.users.register.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.users.register.response.RegisterResponse;
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

    /*private void authenticate(String username, String password) throws Exception{
        try {
            auth
        }
    }*/
}
