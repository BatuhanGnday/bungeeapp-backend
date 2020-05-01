package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.exception.ResourceNotFoundException;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final DatabaseService databaseService;

    @Autowired
    public TagService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public List<Post> getPostByTag(String tag, int pageNumber) {
        int count = databaseService.getTagDao().getCount();
        if (pageNumber > count / 10) {
            throw new ResourceNotFoundException("Not found page number: " + pageNumber);
        }
        return databaseService.getTagDao().getPostsByTag(tag, pageNumber * 10);
    }
}
