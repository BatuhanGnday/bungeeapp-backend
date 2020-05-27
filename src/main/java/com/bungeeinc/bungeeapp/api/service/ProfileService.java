package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.setprivate.response.SetPrivateResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.setprivate.response.SetPrivateResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.show.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.update.request.UpdateProfileRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.update.response.UpdateProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.update.response.UpdateProfileResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.Post;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
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

        Date birthday = profile.getBirthday();

        String banner = profile.getBannerKey();

        String email = profile.getEmail();

        int followingCount = databaseService.getUserFollowingsDao().numberOfFollowed(profile.getUserId());

        int followerCount = databaseService
                .getUserFollowingsDao()
                .numberOfFollowers(profile.getUserId());

        boolean isFollowed = databaseService
                .getUserFollowingsDao()
                .isFollow(userDetails.getId(),
                        profile.getUserId()
                );

        String nickname = profile.getNickname();

        boolean joinedRecently = isJoinedRecently(id);

        boolean isPrivate = profile.isPrivate();

        boolean isVerified = false;

        String profileImage = profile.getProfileImageKey();

        List<Post> posts = databaseService.getPostDao().getByUserId(id);

        return new ProfileResponse(
                id, nickname, biography, isPrivate,
                email, profileImage, banner, birthday,
                blockedByViewer, countryBlock, followingCount,
                isFollowed, followerCount, joinedRecently,
                isVerified, posts
        );
    }

    public UpdateProfileResponse updateProfile(UpdateProfileRequest request, BungeeUserDetails userDetails) {
        BungeeProfile profile = databaseService.getProfileDao().getByUserId(userDetails.getId());

        if (profile == null) {
            return new UpdateProfileResponse(UpdateProfileResponseType.FAILED);
        }

        profile.setNickname(request.getNickname());
        profile.setBiography(request.getBiography());
        profile.setPrivate(request.isPrivate());
        profile.setEmail(request.getEmail());
        profile.setBannerKey(request.getBannerKey());
        profile.setProfileImageKey(request.getProfileImageKey());
        profile.setBirthday(request.getBirthday());

        return new UpdateProfileResponse(UpdateProfileResponseType.SUCCESS);
    }

    private boolean isJoinedRecently(int userId) {
        Date currentTime = new Date();
        long createdOnLong = databaseService.getAccountDao().getById(userId).getCreatedOn().getTime();
        long currentTimeLong = currentTime.getTime();

        return (currentTimeLong - createdOnLong < 259000000L);
    }

    public SetPrivateResponse setPrivate(BungeeUserDetails userDetails) {
        BungeeProfile profile = databaseService.getProfileDao().getByUserId(userDetails.getId());
        if (profile.isPrivate()) {
            return new SetPrivateResponse(SetPrivateResponseType.FAILED);
        }
        profile.setPrivate(true);
        databaseService.getProfileDao().updateProfile(profile);
        return new SetPrivateResponse(SetPrivateResponseType.SUCCESS);
    }
}
