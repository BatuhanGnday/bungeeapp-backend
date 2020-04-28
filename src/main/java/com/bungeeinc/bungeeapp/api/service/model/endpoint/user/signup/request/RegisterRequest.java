package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class RegisterRequest {

    @NonNull
    private String username;

    @NonNull
    private String password;

}
