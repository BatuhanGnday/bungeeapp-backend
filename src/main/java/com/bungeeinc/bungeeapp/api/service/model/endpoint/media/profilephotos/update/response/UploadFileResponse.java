package com.bungeeinc.bungeeapp.api.service.model.endpoint.media.profilephotos.update.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class UploadFileResponse {
    UploadFileResponseType type;
    List<String> fileUUIDs;
}
