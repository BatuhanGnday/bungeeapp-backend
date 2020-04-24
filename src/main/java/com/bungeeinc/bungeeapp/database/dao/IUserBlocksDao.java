package com.bungeeinc.bungeeapp.database.dao;

import com.bungeeinc.bungeeapp.database.models.user.User;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface IUserBlocksDao {

    @SqlUpdate("insert into user_blocks(user_id, blocked_user_id) values (user:id, :blockedId)")
    void block(@BindBean("user") User user, @Bind("blockedId") int id);
}
