package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FollowerService {

    private final DatabaseService databaseService;

    @Autowired
    public FollowerService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * @param activeUser ActiveUser
     * @return list of profiles which are follow active user
     */
    public GetFollowingsResponse getFollowers(Optional<Integer> id, BungeeUserDetails activeUser) {
        List<BungeeProfile> followers;
        if (id.isPresent()) {
            if (!databaseService.getProfileDao().isExistById(id)) {
                log.error("Cannot found the profile with id: " + id.get());

                return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
            }
            followers = new ArrayList<>(
                    databaseService.getUserFollowingsDao().getFollowers(id)
            );

            return new GetFollowingsResponse(followers, GetFollowersResponseType.SUCCESSFUL);
        }
        // create list
        followers = new ArrayList<>(
                databaseService.getUserFollowingsDao().getFollowers(Optional.of(activeUser.getId()))
        );

/*
        if (followers.isEmpty()) {
            return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }
*/
        return new GetFollowingsResponse(followers, GetFollowersResponseType.SUCCESSFUL);
    }

    /**
     * @param user ActiveUser
     * @return list of profiles which are send to active user follow request
     */
    public GetFollowRequestResponse getFollowRequests(BungeeUserDetails user) {

        List<BungeeProfile> followRequests =
                new ArrayList<>(databaseService.getUserFollowingsDao().getIncomingRequests(user.getId()));

        if (followRequests.isEmpty()) {
            return new GetFollowRequestResponse(null);
        }

        return new GetFollowRequestResponse(followRequests);
    }


    public GetFollowersIdsResponse getFollowersIds(
            Optional<Integer> id,
            BungeeUserDetails activeUser) {
        List<Integer> ids;

        if (id.isPresent()) {
            if (!databaseService.getProfileDao().isExistById(id)) {
                return new GetFollowersIdsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
            }

            ids = new ArrayList<>(
                    databaseService.getUserFollowingsDao().getFollowersIds(id)
            );

            return new GetFollowersIdsResponse(ids, GetFollowersResponseType.SUCCESSFUL);
        }

        ids = new ArrayList<>(
                databaseService.getUserFollowingsDao().getFollowersIds(Optional.of(activeUser.getId()))
        );

        return new GetFollowersIdsResponse(ids, GetFollowersResponseType.SUCCESSFUL);
    }
}
