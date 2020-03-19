package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.user.User;
import com.bungeeinc.bungeeapp.database.models.user.UserRole;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface IUserDao {

    @SqlUpdate("insert into user_accounts(username, password, first_name, last_name, email)" +
            " values (:username, :password, :firstName, :lastName, :email)")
    @GetGeneratedKeys
    int createUser(@BindBean User user);

    @SqlQuery("select count(*) from user_accounts where username = :username or email = :email")
    boolean isExistByUsernameOrEmail(@BindBean User user);

    @SqlQuery("select * from user_accounts where username = ?")
    @RegisterColumnMapper(User.Mapper.class)
    User findByUsername(String username);

    @SqlQuery("select * from user_accounts where id = ?")
    @RegisterColumnMapper(User.Mapper.class)
    User getById(int id);

    @SqlQuery("select count(*) from user_followings where" +
            " user_id = :userId and following_user_id = :followingUserId")
    boolean isFollow(@Bind("userId") int userId, @Bind("followingUserId") int followingUserId);

    @SqlUpdate("insert into user_followings(user_id, following_user_id, request_accepted)" +
            " values (:userId, :followingUserId, :accepted)")
    void follow(@Bind("userId") int userId, @Bind("followingUserId") int followingUserId, @Bind("accepted") Boolean isAccepted);

    @SqlQuery("select role from user_accounts where id = ?")
    UserRole getRole(int id);

    @SqlQuery("select count(*) from user_followings where " +
            "following_user_id = ?")
    int numberOfFollowers(int id);

    @SqlQuery("select count(*) from user_followings where " +
            "user_id = ?")
    int numberOfFollowed(int id);

    @SqlQuery("select biography from user_accounts where id = ?")
    String getBiography(int id);

    @SqlQuery("select * from user_accounts inner join user_followings on user_id = user_accounts.id and following_user_id = :id")
    @RegisterColumnMapper(User.Mapper.class)
    List<User> getFollowers(@Bind("id") int id);

    @SqlUpdate("update user_accounts set username = :username, password = :password, role = :role, first_name = firstName," +
            " last_name = :lastName, email = :email, biography = :biography, age = :age, is_deleted = isDeleted, " +
            "image_key = :imageKey, created_on = :createdOn, private = :isPrivate where id = :id")
    void updateUser(@BindBean User user);

    @SqlQuery("select * from user_accounts inner join user_followings on user_id = user_accounts.id and " +
            "following_user_id = :id and request_accepted = 0")
    @RegisterColumnMapper(User.Mapper.class)
    List<User> getFollowRequests(@Bind("id") int id);



}
