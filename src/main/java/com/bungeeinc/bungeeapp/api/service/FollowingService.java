package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.GetFollowingsResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponseType;
import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.ids.GetFollowingsIdsResponse;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.user.User;
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
    public GetFollowingsResponse getFollowings(User user, int id) {
        return new GetFollowingsResponse(userToFollowingUserResponseModel(user,
                databaseService.getUserFollowingsDao().getFollowings(id)), GetFollowersResponseType.SUCCESSFUL);
    }

    /**
     *
     * @param id following user id
     * @return follow type response
     */
    public FollowResponse follow(int id, User user) {

        User followingUser = databaseService.getUserDao().getById(id);

        if (databaseService.getUserFollowingsDao().isFollow(user.getId(), id)) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        if (followingUser.isPrivate()) {
            databaseService.getUserFollowingsDao().follow(user.getId(), followingUser.getId(), Boolean.FALSE);
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

    private List<UserModelSummary> userToFollowingUserResponseModel(User activeUser, List<User> userList) {

        if (userList.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserModelSummary> responseModelList = new ArrayList<>();

        for(User user : userList) {
            int id = user.getId();
            String username = user.getUsername();
            String fullName = user.getFirstName() + " " + user.getLastName();
            String imageKey = user.getImageKey();
            boolean isFollowedByActiveUser = databaseService.getUserFollowingsDao()
                    .isFollow(activeUser.getId(), user.getId());
            responseModelList.add(new
                    UserModelSummary(id, username, fullName, imageKey, isFollowedByActiveUser));
        }
        return responseModelList;
    }

    // TODO: revise the return type and change it as GetOutgoingRequestsResponse
    public GetFollowingsResponse getOutgoingRequests(User user) {
        List<User> users = databaseService.getUserFollowingsDao().getOutgoingRequests(user.getId());
        List<UserModelSummary> responseModelList = new ArrayList<>();

        if (users.isEmpty()) {
            // TODO: change type
            return new GetFollowingsResponse(null, GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }
        for (User user1 : users) {
            int id = user1.getId();
            String username = user1.getUsername();
            String fullName = user1.getFirstName() + " " + user.getLastName();
            String imageKey = user1.getImageKey();

            // TODO: isUserFollowActiveUser
            boolean isFollowedByActiveUser = databaseService.getUserFollowingsDao()
                    .isFollow(user1.getId(), user.getId());

            responseModelList.add(new UserModelSummary(id, username, fullName, imageKey, isFollowedByActiveUser));
        }
        return new GetFollowingsResponse(responseModelList, GetFollowersResponseType.SUCCESSFUL);
    }
}
