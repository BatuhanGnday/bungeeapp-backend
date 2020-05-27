package com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateMentionRequest {
    String text;
    String imageKey;
    int postId;
}
