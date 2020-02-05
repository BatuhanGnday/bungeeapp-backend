package com.bungeeinc.bungeeapp.database.models;

import lombok.Data;

import java.sql.Date;
import java.util.List;

@Data
public class Post {

    private int id;

    private String text;

    private Date date;

    private int likes;

    private byte[] photo;

    private String userId;

    private User user;

    private List<Comment> comments;
}
