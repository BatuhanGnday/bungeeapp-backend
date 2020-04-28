package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.request.ShareRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.response.ShareResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.share.response.ShareResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Post;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final DatabaseService databaseService;

    /**
     * Constructor
     * @param databaseService DatabaseService
     */
    @Autowired
    public PostService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    /**
     * Shares a post
     * @param user User
     * @param request ShareRequest
     * @return ShareResponse
     */
    public ShareResponse share(BungeeUserDetails user, ShareRequest request) {
        databaseService.getPostDao().createPost(new Post(user.getId(),request.getText(), request.getImageKey()));
        return new ShareResponse(ShareResponseType.SUCCESS);
    }

    public GetPostsResponse getPosts(int id) {
        return new GetPostsResponse(databaseService.getPostDao().getByUserId(id), GetPostsResponseType.SUCCESSFUL);
    }
}
