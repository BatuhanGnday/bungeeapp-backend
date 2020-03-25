package com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.ids;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.GetFollowingsResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowingsIdsResponse {

    public List<Integer> ids;
    public GetFollowingsResponseType type;

}
