package com.bungeeinc.bungeeapp.database.dao;

import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface IPostImageDao {

    @SqlUpdate("insert into post_images(post_id, image_uuid) " +
            "values (:postId, :imageUUID)")
    void addImage(int postId, String imageUUID);

    @SqlQuery("select image_uuid from post_images where post_id = :postId")
    List<String> getPostImageUUIDsByPostId(@Bind int postId);
}
