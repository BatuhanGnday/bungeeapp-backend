package com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.show.response;

import com.bungeeinc.bungeeapp.database.models.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private String biography;
    private boolean blockedByViewer;
    private boolean countryBlock;
    private int followCount;
    private boolean isFollowed;
    private int followedByCount;
    private String nickname;
    private int id;
    private boolean isJoinedRecently;
    private boolean isPrivate;
    private boolean isVerified;
    private String profilePicImageKey;
    private String username;
    private List<Post> posts;

}
