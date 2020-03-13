package com.bungeeinc.bungeeapp.api.service.model.endpoint.user.profile.response;

import com.bungeeinc.bungeeapp.database.models.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private String biography;
    private boolean blocked_by_viewer;
    private boolean country_block;
    private int followCount;
    private int followedByCount;
    private String fullName;
    private int id;
    private boolean isJoinedRecently;
    private boolean isPrivate;
    private boolean isVerified;
    private String profilePicImageKey;
    private String username;
    private List<Post> featuredPosts;

}
