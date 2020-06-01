package com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetPostsResponse {
    List<PostContent> posts;
    GetPostsResponseType type;
}
