package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.user.User;
import com.bungeeinc.bungeeapp.database.models.user.UserRole;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
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

    @SqlQuery("select * from user_accounts where id = ?")
    @RegisterColumnMapper(User.Mapper.class)
    User getById(int id);

    @SqlQuery("select role from user_accounts where id = ?")
    UserRole getRole(int id);

    @SqlQuery("select biography from user_accounts where id = ?")
    String getBiography(int id);

    @SqlUpdate("update user_accounts set username = :username, password = :password, role = :role, first_name = firstName," +
            " last_name = :lastName, email = :email, biography = :biography, age = :age, is_deleted = isDeleted, " +
            "image_key = :imageKey, created_on = :createdOn, private = :isPrivate where id = :id")
    void updateUser(@BindBean User user);





}
