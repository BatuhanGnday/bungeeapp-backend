package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.service.TagService;
import com.bungeeinc.bungeeapp.database.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/tags")
public class TagController {

    private TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping("/get_posts")
    public List<Post> getPostsByTag(@RequestParam("tag") String tag,
                                    @RequestParam("page") int page) {
        Page<Post> resultPage = tagService.getPostByTag(tag, page);
        return resultPage.getContent();
    }
}
