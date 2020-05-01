package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.service.TagService;
import com.bungeeinc.bungeeapp.database.models.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @ResponseBody
    public List<Post> getPostsByTag(@RequestParam("tag") String tag,
                                    @RequestParam("page") int page) {
        return tagService.getPostByTag(tag, page);
    }
}
