package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.BungeeProfile;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Optional;

public interface IUserFollowingsDao {

    @SqlQuery("select count(*) from user_followings where " +
            "user_id = ?")
    int numberOfFollowed(int id);

    @SqlQuery("select * from profiles inner join user_followings on " +
            "user_followings.user_id = profiles.user_id and following_user_id = :id " +
            "and request_accepted = 1")
    @RegisterColumnMapper(BungeeProfile.Mapper.class)
    List<BungeeProfile> getFollowers(@Bind("id") Optional<Integer> id);

    @SqlQuery("select user_id from user_followings where following_user_id = :id and request_accepted = 1")
    List<Integer> getFollowersIds(@Bind("id") Optional<Integer> id);

    @SqlQuery("select following_user_id from user_followings where user_id = :id")
    List<Integer> getFollowingsIds(@Bind("id") int id);

    @SqlQuery("select * from profiles inner join user_followings on following_user_id = profiles.user_id and user_id = :id")
    @RegisterColumnMapper(BungeeProfile.Mapper.class)
    List<BungeeProfile> getFollowings(@Bind("id") Optional<Integer> id);

    @SqlQuery("select * from profiles inner join user_followings on user_id = profiles.user_id and " +
            "following_user_id = :id and request_accepted = 0")
    @RegisterColumnMapper(BungeeProfile.Mapper.class)
    List<BungeeProfile> getIncomingRequests(@Bind("id") int id);

    @SqlQuery("select * from profiles inner join user_followings on following_user_id = profiles.user_id and " +
            "user_id = :id and request_accepted = 0")
    @RegisterColumnMapper(BungeeProfile.Mapper.class)
    List<BungeeProfile> getOutgoingRequests(@Bind("id") int id);

    @SqlUpdate("insert into user_followings(user_id, following_user_id, request_accepted)" +
            " values (:userId, :followingUserId, :accepted)")
    void follow(@Bind("userId") int userId, @Bind("followingUserId") int followingUserId, @Bind("accepted") Boolean isAccepted);

    @SqlQuery("select count(*) from user_followings where" +
            " user_id = :userId and following_user_id = :followingUserId")
    boolean isFollow(@Bind("userId") int userId, @Bind("followingUserId") int followingUserId);

    @SqlQuery("select count(*) from user_followings where " +
            "following_user_id = ?")
    int numberOfFollowers(int id);
}
