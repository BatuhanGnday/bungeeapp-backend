package com.bungeeinc.bungeeapp.database.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class Post {

    private int id;

    @NonNull
    private int userId;

    @NonNull
    private String text;

    private String imageKey;

    private Timestamp sharedOn;

    public Post(int userId, String text, String imageKey) {
        this.userId = userId;
        this.text = text;
        this.imageKey = imageKey;
    }

    public static class Mapper implements ColumnMapper<Post> {
        @Override
        public Post map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {

            int id = r.getInt("id");
            int userId = r.getInt("user_id");
            String text = r.getString("text");
            String imageKey = r.getString("image_key");
            Timestamp sharedOn = r.getTimestamp("shared_on");

            Post post = new Post(userId, text, imageKey);
            post.setId(id);
            post.setSharedOn(sharedOn);

            return post;
        }
    }


}
