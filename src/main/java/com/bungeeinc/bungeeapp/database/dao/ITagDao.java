package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.Post;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITagDao {

    @SqlUpdate("insert into tags (post_id, tag) values (:postId, :tag)")
    @GetGeneratedKeys
    int addTag(@Bind("postId") int postId, @Bind("tag") String tag);

    @SqlQuery("select * from posts inner join tags on post_id = posts.id and tags.tag = :tag")
    @RegisterColumnMapper(Post.Mapper.class)
    Page<Post> getPostsWithTag(@Bind("tag") String tag,Pageable pageable);
}
