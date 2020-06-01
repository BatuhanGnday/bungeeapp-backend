package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.model.MediaService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UpdateProfilePhotoResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("profile-photos/update")
    public UpdateProfilePhotoResponse updateProfilePhoto(@RequestParam("imageFile") MultipartFile file,
                                                         @ActiveUser BungeeUserDetails activeUser) {
        return mediaService.updateProfilePhoto(file, activeUser);
    }
}
