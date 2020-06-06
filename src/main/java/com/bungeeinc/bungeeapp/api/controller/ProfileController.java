package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.ProfileService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.setprivate.response.SetPrivateResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.show.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.update.request.UpdateProfileRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.update.response.UpdateProfileResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/profiles")
public class ProfileController {

    private final ProfileService profileService;

    @Autowired
    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    @GetMapping("/{userId}")
    public ProfileResponse show(@PathVariable("userId") int userId, @ActiveUser BungeeUserDetails activeUser) {
        return profileService.showProfile(userId, activeUser);
    }

    @PutMapping("/update")
    public UpdateProfileResponse update(@RequestBody @Valid UpdateProfileRequest request,
                                        @ActiveUser BungeeUserDetails userDetails) {
        return profileService.updateProfile(request, userDetails);
    }

    @PutMapping("/set-private")
    public SetPrivateResponse setPrivate(@ActiveUser BungeeUserDetails userDetails) {
        return profileService.setPrivate(userDetails);
    }

    @GetMapping("/{userId}/posts")
    public GetPostsResponse getPostsResponse(@PathVariable("userId") int userId,
                                             @ActiveUser BungeeUserDetails activeUser) {
        return profileService.getPostsById(userId, activeUser);
    }

    @GetMapping("/feed")
    public GetPostsResponse getFeed(@ActiveUser BungeeUserDetails activeUser) {
        return profileService.getFeed(activeUser);
    }
}
