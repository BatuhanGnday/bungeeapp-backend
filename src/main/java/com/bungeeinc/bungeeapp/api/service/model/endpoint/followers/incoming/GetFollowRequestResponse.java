package com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming;

import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetFollowRequestResponse {
    List<BungeeProfile> followRequests;
}
