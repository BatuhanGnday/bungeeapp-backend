package com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list;

import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetFollowersResponse {
    List<UserModelSummary> followers;
    GetFollowersResponseType type;
}
