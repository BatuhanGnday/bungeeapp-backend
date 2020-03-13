package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.Post;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface IPostDao {

    @SqlQuery("insert into posts (user_id, text, image_key)" +
            " values (?, post:text, post:imageKey)")
    @GetGeneratedKeys
    int createPost(int userId, @BindBean("post") Post post);

    @SqlQuery("select * from posts where user_id = :userId")
    List<Post> getByUserId(@Bind("userId") int userId);
}
