package com.bungeeinc.bungeeapp.database.models;

import lombok.Data;
import lombok.NonNull;

@Data
public class Comment {

    private int id;

    @NonNull
    private String text;

    private int postId;

    private int authorId;

    private String authorName;

    private Post post;

}
