package com.bungeeinc.bungeeapp.api.service.model.endpoint.users.login.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private LoginResponseType type;
    private String token;
}
