package com.bungeeinc.bungeeapp.api.controller;

import com.bungeeinc.bungeeapp.api.annotation.activeuser.ActiveUser;
import com.bungeeinc.bungeeapp.api.service.MentionService;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.request.CreateMentionRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.mentions.create.response.CreateMentionResponse;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/mentions")
public class MentionController {

    private MentionService mentionService;

    @Autowired
    public MentionController(MentionService mentionService) {
        this.mentionService = mentionService;
    }

    @PostMapping("/create")
    public CreateMentionResponse createMention(@ActiveUser User user, @RequestParam(value = "post_id") int postId,
                                               @RequestBody @Valid CreateMentionRequest request) {
        return mentionService.createMention(user, postId, request);
    }
}
