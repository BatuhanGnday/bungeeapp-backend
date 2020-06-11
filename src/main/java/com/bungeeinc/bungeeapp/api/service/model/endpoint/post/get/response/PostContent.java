package com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
public class PostContent {
    String avatarUUID;
    String username;
    String nickname;
    String desc;
    List<String> postImageUUIDs;
    Date sharedOn;
    int likeCount;
}
