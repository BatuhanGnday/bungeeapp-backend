package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.User;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
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
    User getByUsername(String username);

    @SqlQuery("select count(*) from user_accounts where username = ?")
    boolean isExistByUsername(String username);

    @SqlQuery("select count(*) from user_accounts where username = :username or email = :email")
    boolean login(@BindBean User user);

}
