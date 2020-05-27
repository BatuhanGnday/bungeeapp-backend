package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.FollowerService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.ids.GetFollowersIdsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.incoming.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/followers")
public class FollowerController {
    private final FollowerService followerService;

    /**
     * Constructor
     *
     * @param followerService Follower Service
     */
    @Autowired
    public FollowerController(FollowerService followerService) {
        this.followerService = followerService;
    }

    /**
     * returns the list of followers
     *
     * @param id   User ID -optional
     * @param user Active User
     * @return FollowerResponse
     */
    @GetMapping("/list")
    public GetFollowingsResponse getFollowers(
            @RequestParam(value = "user_id", required = false) Optional<Integer> id,
            @ActiveUser BungeeUserDetails user) {

        return followerService.getFollowers(id, user);
    }

    /**
     * returns the incoming follow requests if the active user have private profile
     *
     * @param user Active User
     * @return Follow Requests
     */
    @GetMapping("/incoming")
    public GetFollowRequestResponse getFollowRequestResponse(@ActiveUser BungeeUserDetails user) {
        
        return followerService.getFollowRequests(user);
    }

    /**
     * returns the user ids that follow you
     *
     * @param id
     * @param user
     * @return
     */
    @GetMapping("/ids")
    public GetFollowersIdsResponse getFollowersIds(
            @RequestParam(value = "user_id", required = false) Optional<Integer> id,
            @ActiveUser BungeeUserDetails user) {

        return followerService.getFollowersIds(id, user);
    }
}

//bungeeapp.com/api/profiles/batuhangnday/followers