package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface IProfileDao {

    @SqlUpdate("insert into profiles (user_id, nickname, biography, email, banner_key, profile_image_key, birthday)" +
            " values (:userId, :nickname, :biography, :email, :bannerKey, :profileImageKey, :birthday")
    @GetGeneratedKeys
    int createProfile(@BindBean BungeeProfile profile);

    @SqlQuery("select * from profiles where user_id = :userId")
    @RegisterColumnMapper(BungeeProfile.Mapper.class)
    BungeeProfile getProfileByUserId(int id);

    @SqlUpdate("update profiles set nickname = :nickname, biography = :biography, email = :email," +
            " banner_key = :bannerKey, profile_image_key = :profileImageKey, birthday = :birthday " +
            "where user_id = userId")
    void updateProfile(@BindBean BungeeProfile profile);


}
