package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class LoginRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;
}
