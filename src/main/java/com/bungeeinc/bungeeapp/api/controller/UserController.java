package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.AccountService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.show.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.request.UpdateAccountRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.response.UpdateProfileResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private final AccountService accountService;

    @Autowired
    public UserController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/sign-up")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        return accountService.register(request);
    }

    @PostMapping("/auth")
    public LoginResponse auth(@RequestBody @Valid LoginRequest request) {
        return accountService.login(request);
    }

    @GetMapping("/show")
    public ProfileResponse show(@RequestParam(value = "user_id") int id, @ActiveUser BungeeUserDetails user) {
        return accountService.showProfile(user, id);
    }

    @PutMapping("/update")
    public UpdateProfileResponse updateProfileResponse(@ActiveUser BungeeUserDetails user, @RequestBody @Valid UpdateAccountRequest request) {
        return accountService.updateUser(user, request);
    }


}