package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class FollowRequest {
    @NonNull
    private int followingUserId;
}
