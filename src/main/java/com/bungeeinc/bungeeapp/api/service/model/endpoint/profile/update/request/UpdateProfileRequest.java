package com.bungeeinc.bungeeapp.api.service.model.endpoint.profile.update.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.Date;

@NoArgsConstructor
@Getter
public class UpdateProfileRequest {

    String nickname;

    String biography;

    boolean isPrivate;

    String email;

    String bannerKey;

    String profileImageKey;

    Date birthday;
}
