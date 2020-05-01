package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.GetFollowingsResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.ids.GetFollowingsIdsResponse;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FollowingService {

    private final DatabaseService databaseService;

    /**
     * Constructor
     * @param databaseService Database Service
     */
    @Autowired
    public FollowingService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     *
     * @param user Active user
     * @param id User id you want to get followings
     * @return GetFollowerResponse
     */
    public GetFollowingsResponse getFollowings(BungeeUserDetails user, int id) {
        List<BungeeProfile> followingResponses = new ArrayList<BungeeProfile>(databaseService.getUserFollowingsDao().getFollowings(id));

        if (followingResponses.isEmpty()) {
            return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }

        return new GetFollowingsResponse(followingResponses, GetFollowersResponseType.SUCCESSFUL);
    }

    /**
     *
     * @param id following user id
     * @return follow type response
     */
    public FollowResponse follow(int id, BungeeUserDetails user) {

        BungeeProfile followingProfile = databaseService.getProfileDao().getByUserId(id);

        if (databaseService.getUserFollowingsDao().isFollow(user.getId(), id)) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        if (followingProfile.isPrivate()) {
            databaseService.getUserFollowingsDao().follow(user.getId(), followingProfile.getId(), Boolean.FALSE);
        } else {
            databaseService.getUserFollowingsDao().follow(user.getId(), id, Boolean.TRUE);
        }
        return new FollowResponse(FollowResponseType.SUCCESS);
    }

    /**
     *
     * @param id user id
     * @return a list of following users ids
     */
    public GetFollowingsIdsResponse getFollowingsIds(int id) {
        List<Integer> ids = new ArrayList<>(databaseService.getUserFollowingsDao().getFollowingsIds(id));
        if (ids.isEmpty()) {
            return new GetFollowingsIdsResponse(ids, GetFollowingsResponseType.UNABLE_TO_GET_FOLLOWINGS);
        }
        return new GetFollowingsIdsResponse(ids, GetFollowingsResponseType.SUCCESSFUL);
    }

    public GetFollowingsResponse getOutgoingRequests(BungeeUserDetails user) {

        List<BungeeProfile> outgoingRequests = new ArrayList<>(
                databaseService.getUserFollowingsDao().getOutgoingRequests(user.getId())
        );

        if (outgoingRequests.isEmpty()) {
            return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }

        return new GetFollowingsResponse(outgoingRequests, GetFollowersResponseType.SUCCESSFUL);
    }


}
