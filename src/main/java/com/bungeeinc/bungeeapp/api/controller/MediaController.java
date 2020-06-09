package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.MediaService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UploadFileResponse;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/avatar/update")
    public UploadFileResponse updateProfilePhoto(@RequestParam("imageFile") MultipartFile file,
                                                 @ActiveUser BungeeUserDetails activeUser) {
        return mediaService.updateProfilePhoto(file, activeUser);
    }

    @PostMapping("/post/upload")
    public UploadFileResponse uploadMedia(@RequestParam("imageFiles") MultipartFile[] files) {
        return mediaService.uploadMedia(files);
    }

    @GetMapping("/avatar/owner/{userId}")
    public ResponseEntity<byte[]> getProfilePhoto(@PathVariable int userId) throws IOException {
        return mediaService.getProfilePhoto(userId);
    }
}
