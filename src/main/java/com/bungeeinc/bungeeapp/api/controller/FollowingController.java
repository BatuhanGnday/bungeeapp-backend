package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.FollowingService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowersResponse;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/followings")
public class FollowingController {

    private final FollowingService followingService;

    @Autowired
    public FollowingController(FollowingService followingService) {
        this.followingService = followingService;
    }

    @GetMapping("/list")
    public GetFollowersResponse getFollowings(@RequestParam(value = "user_id") int id, @ActiveUser User user) {
        return followingService.getFollowings(user,id);
    }

    @GetMapping("/ids")
    public GetFollowersIdsResponse getfollowingsIds(@RequestParam(value = "user_id") int id) {
        return followingService.getFollowingsIds(id);
    }

    @PostMapping("/create")
    public FollowResponse follow(@RequestParam(value = "user_id") int id, @ActiveUser User user) {
        return followingService.follow(id, user);
    }

}
