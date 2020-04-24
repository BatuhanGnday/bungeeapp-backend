package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.user.User;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface IUserFollowingsDao {

    @SqlQuery("select count(*) from user_followings where " +
            "user_id = ?")
    int numberOfFollowed(int id);

    @SqlQuery("select * from user_accounts inner join user_followings on user_id = user_accounts.id and following_user_id = :id " +
            "and request_accepted = 1")
    @RegisterColumnMapper(User.Mapper.class)
    List<User> getFollowers(@Bind("id") int id);

    @SqlQuery("select user_id from user_followings where following_user_id = :id and request_accepted = 1")
    List<Integer> getFollowersIds(@Bind("id") int id);

    @SqlQuery("select following_user_id from user_followings where user_id = :id")
    List<Integer> getFollowingsIds(@Bind("id") int id);

    @SqlQuery("select * from user_accounts inner join user_followings on following_user_id = user_accounts.id and user_id = :id")
    @RegisterColumnMapper(User.Mapper.class)
    List<User> getFollowings(@Bind("id") int id);

    @SqlQuery("select * from user_accounts inner join user_followings on user_id = user_accounts.id and " +
            "following_user_id = :id and request_accepted = 0")
    @RegisterColumnMapper(User.Mapper.class)
    List<User> getIncomingRequests(@Bind("id") int id);

    @SqlQuery("select * from user_accounts inner join user_followings on following_user_id = user_accounts.id and " +
            "user_id = :id and request_accepted = 0")
    @RegisterColumnMapper(User.Mapper.class)
    List<User> getOutgoingRequests(@Bind("id") int id);

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
