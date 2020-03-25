package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.Mention;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface IMentionDao {

    @SqlUpdate("insert into mentions(post_id, author_id, text, image_key) " +
            "values (:postId, :authorId, :text, :imageKey)")
    @GetGeneratedKeys
    int createMention(@BindBean Mention mention);

    @SqlQuery("select * from mentions where post_id = :id")
    Mention getMentions(@Bind("id") int postId);
}
