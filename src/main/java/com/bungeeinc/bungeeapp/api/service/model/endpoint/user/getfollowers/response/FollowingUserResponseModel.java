package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FollowingUserResponseModel {
    int id;
    String username;
    String fullName;
    String imageKey;
    boolean isFollowedByActiveUser;
}
