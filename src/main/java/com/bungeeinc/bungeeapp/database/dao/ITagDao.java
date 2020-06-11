package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.Post;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface ITagDao {

    @SqlUpdate("insert into tags (post_id, tag) values (:postId, :tag)")
    void addTag(@Bind("postId") int postId, @Bind("tag") String tag);

    @SqlQuery("select * from posts inner join tags on post_id = posts.id and tags.tag = :tag limit 10 offset :page")
    @RegisterColumnMapper(Post.Mapper.class)
    List<Post> getPostsByTag(@Bind("tag") String tag, @Bind("page") int page);

    @SqlQuery("select count(*) from tags")
    int getCount();
}
