package com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.show.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private final int id;
    private final String nickname;
    private final String biography;
    private final boolean isPrivate;
    private final String email;
    private final String profilePicImageKey;
    private final String bannerKey;
    private final Date birthday;
    private final boolean blockedByViewer;
    private final boolean countryBlock;
    private final int followingCount;
    private final boolean isFollowed;
    private final int followerCount;
    private final boolean isJoinedRecently;
    // TODO: add verified to profile entity
    private final boolean isVerified;
}
