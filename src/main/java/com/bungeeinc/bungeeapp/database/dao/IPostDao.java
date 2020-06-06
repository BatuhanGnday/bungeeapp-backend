package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.Post;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface IPostDao {

    @SqlUpdate("insert into posts (user_id, text, image_key)" +
            " values (:userId, :text, :imageKey)")
    @GetGeneratedKeys
    int createPost(@BindBean() Post post);

    @SqlQuery("select * from posts where user_id = :userId")
    @RegisterColumnMapper(Post.Mapper.class)
    List<Post> getByUserId(@Bind("userId") int userId);

    @SqlQuery("select count(*) from posts where id = :id")
    boolean isExist(@Bind("id") int id);

    @SqlQuery("select * from posts where user_id in " +
            "(select user_followings.following_user_id from user_followings where user_followings.user_id = :id)" +
            " order by shared_on desc;")
    @RegisterColumnMapper(Post.Mapper.class)
    List<Post> getFeed(@Bind("id") int userId);
}
