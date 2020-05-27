package com.bungeeinc.bungeeapp.api.service.model.commonrequests;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Getter
public class UserIdRequest {

    @NonNull
    private int userID;
}
