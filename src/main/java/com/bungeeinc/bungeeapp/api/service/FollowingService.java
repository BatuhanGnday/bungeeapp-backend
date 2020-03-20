package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.FollowingUserResponseModel;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.GetFollowersResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.GetFollowersResponseType;
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

    private List<FollowingUserResponseModel> userToFollowingUserResponseModel(User activeUser, List<User> userList) {

        if (userList.isEmpty()) {
            return Collections.emptyList();
        }

        List<FollowingUserResponseModel> responseModelList = new ArrayList<>();
        for(User user : userList) {
            int id = user.getId();
            String username = user.getUsername();
            String fullName = user.getFirstName() + " " + user.getLastName();
            String imageKey = user.getImageKey();
            boolean isFollowedByActiveUser = databaseService.getUserFollowingsDao()
                    .isFollow(activeUser.getId(), user.getId());
            responseModelList.add(new
                    FollowingUserResponseModel(id, username, fullName, imageKey, isFollowedByActiveUser));
        }
        return responseModelList;
    }


}
