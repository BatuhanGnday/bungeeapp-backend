package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FollowerService {

    private final DatabaseService databaseService;

    @Autowired
    public FollowerService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     *
     * @param activeUser ActiveUser
     * @return list of profiles which are follow active user
     */
    public GetFollowingsResponse getFollowers(BungeeUserDetails activeUser) {

        // create list
        List<BungeeProfile> followers = new ArrayList<>(
                databaseService.getUserFollowingsDao().getFollowers(activeUser.getId())
        );

        if (followers.isEmpty()) {
            return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }

        return new GetFollowingsResponse(followers, GetFollowersResponseType.SUCCESSFUL);
    }

    /**
     *
     * @param user ActiveUser
     * @return list of profiles which are send to active user follow request
     */
    public GetFollowRequestResponse getFollowRequests(BungeeUserDetails user) {

        List<BungeeProfile> followRequests = new ArrayList<>(
                databaseService.getUserFollowingsDao().getIncomingRequests(user.getId())
        );

        if (followRequests.isEmpty()) {
            return new GetFollowRequestResponse(null);
        }

        return new GetFollowRequestResponse(followRequests);
    }


    public GetFollowersIdsResponse getFollowersIds(int id) {
        List<Integer> ids = new ArrayList<>(databaseService.getUserFollowingsDao().getFollowersIds(id));
        if (ids.isEmpty()) {
            return new GetFollowersIdsResponse(Collections.emptyList(), GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }
        return new GetFollowersIdsResponse(ids, GetFollowersResponseType.SUCCESSFUL);
    }
}
