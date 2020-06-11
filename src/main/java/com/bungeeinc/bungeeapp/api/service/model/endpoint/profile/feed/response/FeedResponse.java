package com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.feed.response;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.PostContent;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class FeedResponse {
    List<PostContent> posts;
}
