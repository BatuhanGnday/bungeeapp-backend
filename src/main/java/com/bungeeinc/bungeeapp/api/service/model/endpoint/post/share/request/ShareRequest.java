package com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class ShareRequest {
    String text;
    String[] imageUUIDs;
}
