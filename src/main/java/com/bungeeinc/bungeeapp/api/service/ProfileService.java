package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.show.response.ProfileResponse;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.Post;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProfileService {

    private final DatabaseService databaseService;

    @Autowired
    public ProfileService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public ProfileResponse showProfile(int id, BungeeUserDetails userDetails) {
        BungeeProfile profile = databaseService.getProfileDao().getByUserId(id);

        String biography = profile.getBiography();
        boolean blockedByViewer = false;
        boolean countryBlock = false;
        int followCount = databaseService.getUserFollowingsDao().numberOfFollowed(profile.getUserId());
        boolean isFollowed = databaseService.getUserFollowingsDao().isFollow(userDetails.getId(), profile.getUserId());
        int followedByCount = databaseService.getUserFollowingsDao().numberOfFollowers(profile.getUserId());
        String nickname = profile.getNickname();
        int profileUserId = profile.getUserId();
        boolean joinedRecently = isJoinedRecently(id);
        boolean isPrivate = profile.isPrivate();
        boolean isVerified = false;
        String profileImage = profile.getProfileImageKey();
        String username = databaseService.getAccountDao().getById(id).getUsername();
        List<Post> posts = databaseService.getPostDao().getByUserId(id);

        return new ProfileResponse(biography, blockedByViewer, countryBlock, followCount, isFollowed, followedByCount,
                nickname, profileUserId, joinedRecently, isPrivate, isVerified, profileImage, username, posts);
    }

    private boolean isJoinedRecently(int userId) {
        Date currentTime = new Date();
        long createdOnLong = databaseService.getAccountDao().getById(userId).getCreatedOn().getTime();
        long currentTimeLong = currentTime.getTime();

        return (currentTimeLong - createdOnLong < 259000000L);
    }
}
