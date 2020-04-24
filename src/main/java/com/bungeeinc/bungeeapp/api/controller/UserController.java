package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.UserService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.show.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.request.UpdateProfileRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.response.UpdateProfileResponse;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/sign-up")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        return userService.register(request);
    }

    @PostMapping("/auth")
    public LoginResponse auth(@RequestBody @Valid LoginRequest request) {
        return userService.auth(request);
    }

    @GetMapping("/show")
    public ProfileResponse show(@RequestParam(value = "user_id") int id, @ActiveUser User user) {
        return userService.showProfile(user, id);
    }

    @PutMapping("/update")
    public UpdateProfileResponse updateProfileResponse(@ActiveUser User user, @RequestBody @Valid UpdateProfileRequest request) {
        return userService.updateUser(user, request);
    }

}