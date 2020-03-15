package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.service.UserService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.request.FollowRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.websocket.server.PathParam;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    private HttpServletRequest servletRequest;

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

/*    @GetMapping("{profileId}")
    public ProfileResponse getProfile(@PathVariable("profileId")String username) {

    }*/

/*    @GetMapping("{profileId}")
    public ProfileResponse profileResponse(@PathVariable String profileId) {

    }*/

    // TODO:
    @PostMapping("/follow")
    public FollowResponse follow(@RequestBody @Valid FollowRequest request) {
        return userService.follow(request, servletRequest);
    }

}
