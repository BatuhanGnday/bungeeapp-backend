package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowersResponse {
    List<FollowingUserResponseModel> followers;
    GetFollowersResponseType type;
}
