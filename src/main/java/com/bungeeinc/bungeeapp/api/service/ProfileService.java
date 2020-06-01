package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.exception.NoSuchProfileException;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.PostContent;
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

import java.util.ArrayList;
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

        String email = profile.getEmail();

        String banner = profile.getBannerKey();

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

        if (profile.isPrivate()) {
            email = null;
        }

        return new ProfileResponse(
                id, nickname, biography, isPrivate,
                email, profileImage, banner, birthday,
                blockedByViewer, countryBlock, followingCount,
                isFollowed, followerCount, joinedRecently,
                isVerified
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
        if (databaseService.getAccountDao().getById(userId) == null) {
            throw new NoSuchProfileException();
        }
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

    /**
     * @param userId     profile id clients wants to see its posts.
     * @param activeUser client
     * @return Post List
     */
    public GetPostsResponse getPostsById(int userId, BungeeUserDetails activeUser) {
        BungeeProfile profile = databaseService.getProfileDao().getByUserId(userId);
        BungeeUserDetails account = databaseService.getAccountDao().getById(userId);

        BungeeProfile activeProfile = databaseService.getProfileDao().getByUserId(activeUser.getId());

        if (profile.isPrivate()) {
            if (!databaseService.getUserFollowingsDao().isFollow(activeProfile.getUserId(), profile.getUserId())) {
                return new GetPostsResponse(null, GetPostsResponseType.FAILED);
            }
        }

        List<Post> posts = databaseService.getPostDao().getByUserId(userId);
        List<PostContent> contents = new ArrayList<>();

        for (Post post : posts) {
            String profileImage = profile.getProfileImageKey();
            String username = account.getUsername();
            String nickname = profile.getNickname();
            String text = post.getText();
            Date sharedOn = post.getSharedOn();
            String image = post.getImageKey();
            // TODO: implement like service
            int numOfLike = 100;
            contents.add(new PostContent(profileImage, username, nickname, text, sharedOn, image, numOfLike));
        }

        return new GetPostsResponse(contents, GetPostsResponseType.SUCCESSFUL);
    }
}
