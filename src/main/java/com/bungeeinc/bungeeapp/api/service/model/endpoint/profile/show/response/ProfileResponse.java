package com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.show.response;

import com.bungeeinc.bungeeapp.database.models.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private int id;
    private String nickname;
    private String biography;
    private boolean isPrivate;
    private String email;
    private String profilePicImageKey;
    private String bannerKey;
    private Date birthday;
    private boolean blockedByViewer;
    private boolean countryBlock;
    private int followingCount;
    private boolean isFollowed;
    private int followerCount;
    private boolean isJoinedRecently;
    // TODO: add verified to profile entity
    private boolean isVerified;
    private List<Post> posts;

}
