package com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class PostContent {
    int userId;
    String nickname;
    String text;
    Date sharedOn;
    String image;
    int numOfLike;
}
