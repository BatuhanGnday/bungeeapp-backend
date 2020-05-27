package com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowingsResponse {
    List<BungeeProfile> followings;

    //TODO: Change type
    GetFollowersResponseType type;
}
