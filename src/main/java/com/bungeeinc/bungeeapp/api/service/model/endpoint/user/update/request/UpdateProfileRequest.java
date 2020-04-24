package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UpdateProfileRequest {
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String biography;
    int age;
    String imageKey;
    boolean isPrivate;
}
