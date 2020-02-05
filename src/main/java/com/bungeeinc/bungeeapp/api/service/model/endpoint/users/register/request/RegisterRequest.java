package com.bungeeinc.bungeeapp.api.service.model.endpoint.users.register.request;

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

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    @NonNull
    private String email;
}
