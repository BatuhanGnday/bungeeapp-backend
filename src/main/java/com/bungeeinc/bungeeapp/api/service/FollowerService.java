package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
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
public class FollowerService {

    private final DatabaseService databaseService;

    @Autowired
    public FollowerService(DatabaseService databaseService, UserService userService) {
        this.databaseService = databaseService;
    }

    public GetFollowersResponse getFollowers(User activeUser, int id) {

        return new GetFollowersResponse(userToFollowingUserResponseModel(activeUser,
                databaseService.getUserFollowingsDao().getFollowers(id)), GetFollowersResponseType.SUCCESSFUL);
    }

    public GetFollowRequestResponse getFollowRequests(User user) {
        return new GetFollowRequestResponse(userToFollowingUserResponseModel(user,
                databaseService.getUserFollowingsDao().getFollowRequests(user.getId())));
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


    public GetFollowersIdsResponse getFollowersIds(int id) {
        List<Integer> ids = new ArrayList<>(databaseService.getUserFollowingsDao().getFollowersIds(id));
        if (ids.isEmpty()) {
            return new GetFollowersIdsResponse(Collections.emptyList(), GetFollowersResponseType.UNABLE_TO_GET_FOLLOWERS);
        }
        return new GetFollowersIdsResponse(ids, GetFollowersResponseType.SUCCESSFUL);
    }
}
