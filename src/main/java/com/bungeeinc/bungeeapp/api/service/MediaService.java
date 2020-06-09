package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.config.Config;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UploadFileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response.UploadFileResponseType;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class MediaService {

    private final DatabaseService databaseService;

    @Autowired
    public MediaService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public UploadFileResponse updateProfilePhoto(MultipartFile file, BungeeUserDetails activeUser) {

        BungeeProfile activeProfile = databaseService.getProfileDao().getByUserId(activeUser.getId());

        if (!file.getContentType().startsWith("image/")) {
            log.warn("Invalid file extension. File type was: " + file.getContentType());
            return new UploadFileResponse(UploadFileResponseType.INVALID_FILE_TYPE, null);
        }

        String fileName = file.getOriginalFilename().replace(file.getOriginalFilename(),
                FilenameUtils.getBaseName(UUID.randomUUID().toString()) + "."
                        + FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase()));

        Path fileNameAndPath = Paths.get(Config.AVATAR_DIRECTORY, fileName);
        List<String> fileUUIDs = new ArrayList<>();

        activeProfile.setAvatarUUID(fileName);
        databaseService.getProfileDao().updateProfile(activeProfile);
        fileUUIDs.add(fileName);

        try {
            Files.write(fileNameAndPath, file.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new UploadFileResponse(UploadFileResponseType.SUCCESSFUL, fileUUIDs);

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

    public UploadFileResponse uploadMedia(MultipartFile[] files) {
        if (files.length > 4) {
            log.warn("File limit exceeded while uploading files.");
            return new UploadFileResponse(UploadFileResponseType.FILE_LIMIT_EXCEEDED, null);
        }

        for (MultipartFile file : files) {
            if (!file.getContentType().startsWith("image/")) {
                log.warn("Invalid file extension. File type was: " + file.getContentType());
                return new UploadFileResponse(UploadFileResponseType.INVALID_FILE_TYPE, null);
            }
        }

        List<String> fileUUIDs = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename().replace(file.getOriginalFilename(),
                    FilenameUtils.getBaseName(UUID.randomUUID().toString()) + "."
                            + FilenameUtils.getExtension(file.getOriginalFilename().toLowerCase()));

            Path fileNameAndPath = Paths.get(Config.POST_MEDIA_DIRECTORY, fileName);
            fileUUIDs.add(fileName);
            try {
                Files.write(fileNameAndPath, file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return new UploadFileResponse(UploadFileResponseType.SUCCESSFUL, fileUUIDs);
    }
}
