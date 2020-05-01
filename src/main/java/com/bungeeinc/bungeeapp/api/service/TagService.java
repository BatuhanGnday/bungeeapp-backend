package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.exception.ResourceNotFoundException;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.post.get.response.GetPostsResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class TagService {

    private final DatabaseService databaseService;

    @Autowired
    public TagService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public Page<Post> getPostByTag(String tag,int pageNumber){

        PageRequest pageable = PageRequest.of(pageNumber - 1, 2);
        Page<Post> resultPage = databaseService.getTagDao().getPostsWithTag(tag, pageable);

        if (pageNumber > resultPage.getTotalPages()) {
            throw new ResourceNotFoundException();
        }
    return resultPage;
    }
}
