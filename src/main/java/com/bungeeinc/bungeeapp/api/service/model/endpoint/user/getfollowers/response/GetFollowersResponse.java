package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response;

import com.bungeeinc.bungeeapp.database.models.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowersResponse {
    List<User> followers;
    GetFollowersResponseType type;
}
