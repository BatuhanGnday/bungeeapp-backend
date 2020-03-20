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
public class Mention {

    private int id;

    private int postId;

    private int authorId;

    @NonNull
    private String text;

    private String imageKey;

    private Timestamp createdOn;

    public Mention (int postId, int authorId, String text, String imageKey) {
        this.postId = postId;
        this.authorId = authorId;
        this.text = text;
        this.imageKey = imageKey;
    }

    public static class Mapper implements ColumnMapper<Mention> {

        @Override
        public Mention map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
            int id = r.getInt("id");
            int postId = r.getInt("post_id");
            int authorId = r.getInt("author_id");
            String text = r.getString("text");
            String imageKey = r.getString("image_key");
            Timestamp createdOn = r.getTimestamp("created_on");

            Mention mention = new Mention(postId, authorId, text, imageKey);
            mention.setId(id);
            mention.setCreatedOn(createdOn);

            return mention;
        }
    }


}
