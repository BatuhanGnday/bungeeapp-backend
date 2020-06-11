package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.jdbi.v3.sqlobject.config.RegisterColumnMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface IAccountDao {

    @SqlUpdate("insert into accounts(username, password)" +
            " values (:username, :password)")
    @GetGeneratedKeys
    int createUser(@BindBean BungeeUserDetails user);

    @SqlQuery("select count(*) from accounts where username = :username")
    boolean isExistByUsername(@Bind String username);

    @SqlQuery("select * from accounts where username = ?")
    @RegisterColumnMapper(BungeeUserDetails.Mapper.class)
    BungeeUserDetails findByUsername(String username);

    @SqlQuery("select * from accounts where id = ?")
    @RegisterColumnMapper(BungeeUserDetails.Mapper.class)
    BungeeUserDetails getById(int id);

    @SqlQuery("select * from accounts where username = ?")
    @RegisterColumnMapper(BungeeUserDetails.Mapper.class)
    BungeeUserDetails getByUsername(String username);

    @SqlUpdate("update accounts set username = :username, password = :password, deleted = :isDeleted, enabled = :isEnabled")
    void updateUser(@BindBean BungeeUserDetails user);

    @SqlQuery("select * from accounts where id in (<ids>)")
    @RegisterColumnMapper(BungeeUserDetails.Mapper.class)
    List<BungeeUserDetails> getAccountsByIdList(@BindList("ids") List<Integer> ids);


}
