package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UpdateProfilePhotoResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UpdateProfilePhotoResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Slf4j
public class MediaService {

    private final DatabaseService databaseService;

    private static String UploadDirectory = System.getProperty("user.dir") + "/uploads";

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

        Path fileNameAndPath = Paths.get(UploadDirectory, fileName);

        activeProfile.setProfileImageKey(fileName);
        databaseService.getProfileDao().updateProfile(activeProfile);

        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UpdateProfilePhotoResponse(UpdateProfilePhotoResponseType.SUCCESSFUL);

    }
}
