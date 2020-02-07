package com.bungeeinc.bungeeapp.database.models;

import lombok.Data;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

@Data
public class Comment {

    private int id;

    private int postId;

    private int authorId;

    @NonNull
    private String text;

    private Timestamp createdOn;

    public static class Mapper implements ColumnMapper<Comment> {

        @Override
        public Comment map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
            int id = r.getInt("id");
            int postId = r.getInt("post_id");
            int authorId = r.getInt("author_id");
            String text = r.getString("text");
            Timestamp createdOn = r.getTimestamp("created_on");

            Comment comment = new Comment(text);
            comment.setId(id);
            comment.setCreatedOn(createdOn);
            //comment.setAuthorId(authorId);
            //comment.setPostId(postId);

            return comment;
        }
    }


}
