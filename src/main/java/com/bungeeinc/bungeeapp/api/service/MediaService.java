package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.config.Config;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UpdateProfilePhotoResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UpdateProfilePhotoResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class MediaService {

    private final DatabaseService databaseService;

    @Autowired
    public MediaService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public UpdateProfilePhotoResponse updateProfilePhoto(MultipartFile file, BungeeUserDetails activeUser) {

        BungeeProfile activeProfile = databaseService.getProfileDao().getByUserId(activeUser.getId());

        if (!file.getContentType().startsWith("image/")) {
            log.warn("Invalid file extension. File type was: " + file.getContentType());
            return new UpdateProfilePhotoResponse(UpdateProfilePhotoResponseType.INVALID_FILE_TYPE);
        }

        String fileName = file.getOriginalFilename().replace(file.getOriginalFilename(),
                FilenameUtils.getBaseName(UUID.randomUUID().toString()) + "."
                        + FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase()));

        Path fileNameAndPath = Paths.get(Config.AVATAR_DIRECTORY, fileName);

        activeProfile.setAvatarUUID(fileName);
        databaseService.getProfileDao().updateProfile(activeProfile);

        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UpdateProfilePhotoResponse(UpdateProfilePhotoResponseType.SUCCESSFUL);

    }

    public ResponseEntity<byte[]> getProfilePhoto(int userId) throws IOException {
        BungeeProfile profile = databaseService.getProfileDao().getByUserId(userId);
        String avatarUUID = profile.getAvatarUUID();
        File file = new File(Config.AVATAR_DIRECTORY + "/" + avatarUUID);

        if (!file.exists()) {
            log.warn("File does not exist. File name was: " + avatarUUID);
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.IMAGE_JPEG)
                .body(Files.readAllBytes(file.toPath()));

    }
}
