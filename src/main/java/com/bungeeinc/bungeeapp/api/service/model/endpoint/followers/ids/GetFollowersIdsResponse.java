package com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowersIdsResponse {

    public List<Integer> ids;
    public GetFollowersResponseType type;

}
