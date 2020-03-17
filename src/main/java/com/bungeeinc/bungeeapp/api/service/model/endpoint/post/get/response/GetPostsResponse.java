package com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response;

import com.bungeeinc.bungeeapp.database.models.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class GetPostsResponse {
    List<Post> posts;
    GetPostsResponseType type;
}
