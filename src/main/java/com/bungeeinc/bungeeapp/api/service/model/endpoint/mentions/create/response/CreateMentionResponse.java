package com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.response;

import com.bungeeinc.bungeeapp.database.models.Mention;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateMentionResponse {
    Mention mention;
    CreateMentionResponseType type;
}
