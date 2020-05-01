package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.service.AccountService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.account.auth.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.account.auth.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.account.signup.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.account.signup.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/sign-up")
    public RegisterResponse register(@RequestBody @Valid RegisterRequest request) {
        return accountService.register(request);
    }

    @PostMapping("/login")
    public LoginResponse auth(@RequestBody @Valid LoginRequest request) {
        return accountService.login(request);
    }


}