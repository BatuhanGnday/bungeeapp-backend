package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.user.User;
import com.bungeeinc.bungeeapp.database.models.user.UserRole;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

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

    // User -> UserDetails
    @SqlQuery("select * from user_accounts where id = ?")
    @RegisterColumnMapper(User.Mapper.class)
    User getById(int id);

    @SqlQuery("select count(*) from user_accounts where username = ?")
    boolean isExistByUsername(String username);

    @SqlQuery("select count(*) from user_accounts where username = :username or email = :email")
    boolean login(@BindBean User user);

    @SqlQuery("select count(*) from user_followings where" +
            " user_id = :userId and following_user_id = :followingUserId")
    boolean isFollow(@Bind("userId") int userId, @Bind("followingUserId") int followingUserId);

    @SqlUpdate("insert into user_followings(user_id, following_user_id)" +
            " values (:userId, :followingUserId)")
    void follow(@Bind("userId") int userId, @Bind("followingUserId") int followingUserId);

    @SqlQuery("select role from user_accounts where id = ?")
    UserRole getRole(int id);

    @SqlQuery("select count(*) from user_followings where " +
            "following_user_id = ?")
    int numberOfFollowers(int id);

    @SqlQuery("select count(*) from user_followings where " +
            "user_id = ?")
    int numberOfFollowed(int id);

}
