package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.request.CreateMentionRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.response.CreateMentionResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.response.CreateMentionResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Mention;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MentionService {

    private final DatabaseService databaseService;

    @Autowired
    public MentionService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public CreateMentionResponse createMention(BungeeUserDetails user, int postId, CreateMentionRequest request) {

        if (!databaseService.getPostDao().isExist(postId)) {
            return new CreateMentionResponse(null, CreateMentionResponseType.POST_DOES_NOT_EXISTS);
        }

        Mention mention = new Mention(
                postId,
                user.getId(),
                request.getText(),
                request.getImageKey()
        );

        mention.setId(
                databaseService.getMentionDao().createMention(mention)
        );

        return new CreateMentionResponse(mention, CreateMentionResponseType.SUCCESSFUL);
    }
}
