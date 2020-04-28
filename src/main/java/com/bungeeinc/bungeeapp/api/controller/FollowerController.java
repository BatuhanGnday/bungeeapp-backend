package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.FollowerService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/followers")
public class FollowerController {

    private final FollowerService followerService;

    @Autowired
    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }


    @GetMapping("/list")
    public GetFollowingsResponse getFollowers(@ActiveUser BungeeUserDetails user){
        return followerService.getFollowers(user);
    }

    @GetMapping("/incoming")
    public GetFollowRequestResponse getFollowRequestResponse(@ActiveUser BungeeUserDetails user) {
        return followerService.getFollowRequests(user);
    }

    @GetMapping("/ids")
    public GetFollowersIdsResponse getFollowersIds(@RequestParam(value = "user_id") int id) {
        return followerService.getFollowersIds(id);
    }


}
