package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.commonrequests.UserIdRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.GetFollowingsResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.ids.GetFollowingsIdsResponse;
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
public class FollowingService {

    private final DatabaseService databaseService;

    /**
     * Constructor
     *
     * @param databaseService Database Service
     */
    @Autowired
    public FollowingService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * @param user Active user
     * @param id   User id you want to get followings
     * @return GetFollowerResponse
     */
    public GetFollowingsResponse getFollowings(
            Optional<Integer> id,
            BungeeUserDetails user) {
        List<BungeeProfile> followings;

        if (id.isPresent()) {
            if (!databaseService.getProfileDao().isExistById(id)) {
                return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
            }

            followings = new ArrayList<>(
                    databaseService.getUserFollowingsDao().getFollowings(id)
            );

            return new GetFollowingsResponse(followings, GetFollowersResponseType.SUCCESSFUL);
        }

        followings = new ArrayList<>(
                databaseService.getUserFollowingsDao().getFollowings(Optional.of(user.getId()))
        );

        return new GetFollowingsResponse(followings, GetFollowersResponseType.SUCCESSFUL);
    }

    /**
     * @param request following user id
     * @return follow type response
     */

    // TODO: check if requestID is null
    public FollowResponse follow(UserIdRequest request, BungeeUserDetails user) {
        if (!databaseService.getProfileDao().isExistById(java.util.Optional.of(request.getUserID()))) {
            log.error("No such profile with id: " + request.getUserID());
            return new FollowResponse(FollowResponseType.NO_SUCH_PROFILE);
        }

        BungeeProfile followingProfile = databaseService.getProfileDao().getByUserId(request.getUserID());

        if (databaseService.getUserFollowingsDao().isFollow(user.getId(), request.getUserID())) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        if (followingProfile.isPrivate()) {
            databaseService.getUserFollowingsDao().follow(user.getId(), followingProfile.getUserId(), Boolean.FALSE);
        } else {
            databaseService.getUserFollowingsDao().follow(user.getId(), request.getUserID(), Boolean.TRUE);
        }
        return new FollowResponse(FollowResponseType.SUCCESS);
    }

    /**
     * @param id   request param
     * @param user Active User
     * @return Followings ids
     */
    public GetFollowingsIdsResponse getFollowingsIds(Optional<Integer> id, BungeeUserDetails user) {
        List<Integer> ids;

        if (id.isPresent()) {
            if (!databaseService.getProfileDao().isExistById(id)) {
                return new GetFollowingsIdsResponse(null, GetFollowingsResponseType.UNABLE_TO_GET_FOLLOWINGS);
            }

            ids = new ArrayList<>(
                    databaseService.getUserFollowingsDao().getFollowersIds(id)
            );

            return new GetFollowingsIdsResponse(ids, GetFollowingsResponseType.SUCCESSFUL);
        }

        ids = new ArrayList<>(
                databaseService.getUserFollowingsDao().getFollowersIds(Optional.of(user.getId()))
        );

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
