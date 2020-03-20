package com.bungeeinc.bungeeapp.api.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserModelSummary {
    int id;
    String username;
    String fullName;
    String imageKey;
    boolean isFollowedByActiveUser;
}
