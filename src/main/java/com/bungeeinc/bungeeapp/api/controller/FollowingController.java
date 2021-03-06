package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.FollowingService;
import com.bungeeinc.bungeeapp.api.service.model.commonrequests.UserIdRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followers.list.GetFollowingsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.create.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.followings.ids.GetFollowingsIdsResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/followings")
public class FollowingController {

    private final FollowingService followingService;

    @Autowired
    public FollowingController(FollowingService followingService) {
        this.followingService = followingService;
    }

    @GetMapping("/list")
    public GetFollowingsResponse getFollowings(
            @RequestParam(value = "user_id", required = false) Optional<Integer> id,
            @ActiveUser BungeeUserDetails user) {

        return followingService.getFollowings(id, user);
    }

    @GetMapping("/ids")
    public GetFollowingsIdsResponse getFollowingsIds(
            @RequestParam(value = "user_id", required = false) Optional<Integer> id,
            @ActiveUser BungeeUserDetails user) {

        return followingService.getFollowingsIds(id, user);
    }

    @PostMapping("/create")
    public FollowResponse follow(@RequestBody @Valid UserIdRequest id, @ActiveUser BungeeUserDetails user) {

        return followingService.follow(id, user);
    }

    @GetMapping("/outgoing")
    public GetFollowingsResponse outgoingRequests(@ActiveUser BungeeUserDetails user) {

        return followingService.getOutgoingRequests(user);
    }

}
