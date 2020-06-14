package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.exception.NoSuchProfileException;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.PostContent;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.feed.response.FeedResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.get.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.setprivate.response.SetPrivateResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.setprivate.response.SetPrivateResponseType;
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

import java.util.*;

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

        String avatarUUID = profile.getAvatarUUID();

        if (profile.isPrivate()) {
            email = null;
        }

        return new ProfileResponse(
                id, nickname, biography, isPrivate,
                email, avatarUUID, banner, birthday,
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
        profile.setAvatarUUID(request.getAvatarUUID());
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
        BungeeProfile activeProfile = databaseService.getProfileDao().getByUserId(activeUser.getId());

        if (profile.isPrivate()) {
            if (!databaseService.getUserFollowingsDao().isFollow(activeProfile.getUserId(), profile.getUserId())) {
                return new GetPostsResponse(null, GetPostsResponseType.FAILED);
            }
        }

        List<Post> posts = databaseService.getPostDao().getByUserId(userId);
        List<PostContent> contents = convertPostListToPostContentList(posts);

        return new GetPostsResponse(contents, GetPostsResponseType.SUCCESSFUL);
    }

    public FeedResponse getFeed(BungeeUserDetails activeUser) {
        List<Post> posts = new ArrayList<>(databaseService.getPostDao().getFeed(activeUser.getId()));
        List<PostContent> feed = convertPostListToPostContentList(posts);

        return new FeedResponse(feed);
    }

    private List<PostContent> convertPostListToPostContentList(List<Post> postList) {
        if (postList.isEmpty()) {
            return Collections.emptyList();
        }

        List<PostContent> contents = new ArrayList<>();

        String avatarUUID;
        String username;
        String nickname;
        String desc;
        List<String> postImageUUIDs;
        Date sharedOn;
        int likeCount;

        List<Integer> ids = new ArrayList<>();

        for (Post post : postList) {
            ids.add(post.getUserId());
        }

        List<BungeeUserDetails> accounts = databaseService.getAccountDao().getAccountsByIdList(ids);
        List<BungeeProfile> profiles = databaseService.getProfileDao().getProfilesByIdList(ids);

        for (Post post : postList) {
            Optional<BungeeProfile> bungeeProfile = profiles.stream()
                    .filter(profile -> profile.getUserId() == post.getUserId())
                    .findFirst();
            assert bungeeProfile.isPresent() : "bungee profile must be present here";

            Optional<BungeeUserDetails> bungeeUserDetails = accounts.stream()
                    .filter(account -> account.getId() == post.getUserId())
                    .findFirst();

            assert bungeeUserDetails.isPresent() : "bungeeUserDetails must be present";

            avatarUUID = bungeeProfile.get().getAvatarUUID();
            username = bungeeUserDetails.get().getUsername();
            nickname = bungeeProfile.get().getNickname();

            desc = post.getText();
            postImageUUIDs = databaseService.getPostImageDao().getPostImageUUIDsByPostId(post.getId());
            sharedOn = post.getSharedOn();
            // TODO: implement
            likeCount = 100;
            contents.add(new PostContent(avatarUUID, username, nickname, desc, postImageUUIDs, sharedOn, likeCount));
        }

        return contents;
    }
}
