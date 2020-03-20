package com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming;

import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFollowRequestResponse {
    List<UserModelSummary> followRequests;
}
