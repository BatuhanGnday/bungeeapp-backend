package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.helper.post.PostHelper;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.request.ShareRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.response.ShareResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.response.ShareResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Post;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final DatabaseService databaseService;

    /**
     * Constructor
     *
     * @param databaseService DatabaseService
     */
    @Autowired
    public PostService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Shares a post
     *
     * @param user    User
     * @param request ShareRequest
     * @return ShareResponse
     */
    public ShareResponse share(BungeeUserDetails user, ShareRequest request) {

        Post post = new Post(user.getId(), request.getText());
        List tags = new ArrayList(PostHelper.findTags(request.getText()));

        if (!databaseService.getProfileDao().isExistById(Optional.of(user.getId()))) {
            return new ShareResponse(ShareResponseType.INVALID_AUTHOR);
        }

        if (request.getText().length() > 140) {
            return new ShareResponse(ShareResponseType.OUT_OF_LIMIT);
        }

        post.setId(databaseService.getPostDao().createPost(post));

        for (Object tag : tags) {
            databaseService.getTagDao().addTag(post.getId(), tag.toString());
        }

        for (String imageUUID : request.getImageUUIDs()) {
            databaseService.getPostImageDao().addImage(post.getId(), imageUUID);
        }


        return new ShareResponse(ShareResponseType.SUCCESS);
    }
}
