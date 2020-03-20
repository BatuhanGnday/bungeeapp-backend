package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowersResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.GetFollowersResponseType;
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

    @Autowired
    public FollowingService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public GetFollowersResponse getFollowings(User user, int id) {
        return new GetFollowersResponse(userToFollowingUserResponseModel(user,
                databaseService.getUserFollowingsDao().getFollowings(id)), GetFollowersResponseType.SUCCESSFUL);
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

    public GetFollowRequestResponse getFollowRequests(User user) {
        return new GetFollowRequestResponse(userToFollowingUserResponseModel(user,
                databaseService.getUserFollowingsDao().getFollowRequests(user.getId())));
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
    public GetFollowersIdsResponse getFollowingsIds(int id) {
        List<Integer> ids = new ArrayList<>(databaseService.getUserFollowingsDao().getFollowingsIds(id));
        if (ids.isEmpty()) {
            return new GetFollowersIdsResponse(Collections.emptyList(),GetFollowersResponseType.UNABLE_TO_GET_FOLLOWINGS);
        }
        return new GetFollowersIdsResponse(ids, GetFollowersResponseType.SUCCESSFUL);
    }
}
