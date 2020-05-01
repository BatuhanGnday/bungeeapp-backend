package com.bungeeinc.bungeeapp.database.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface ITagDao {

    @SqlUpdate("insert into tags (post_id, tag) values (:postId, :tag)")
    @GetGeneratedKeys
    int addTag(@Bind("postId") int postId, @Bind("tag") String tag);
}
