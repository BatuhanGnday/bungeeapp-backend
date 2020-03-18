package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.followrequest;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.FollowingUserResponseModel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFollowRequestResponse {
    List<FollowingUserResponseModel> followRequests;
}
