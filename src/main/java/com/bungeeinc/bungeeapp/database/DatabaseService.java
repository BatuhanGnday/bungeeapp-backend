package com.bungeeinc.bungeeapp.database;

import com.bungeeinc.bungeeapp.database.dao.IMentionDao;
import com.bungeeinc.bungeeapp.database.dao.IPostDao;
import com.bungeeinc.bungeeapp.database.dao.IUserDao;
import com.bungeeinc.bungeeapp.database.dao.IUserFollowingsDao;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Getter
    private IUserDao userDao;

    @Getter
    private IPostDao postDao;

    @Getter
    private IUserFollowingsDao userFollowingsDao;

    @Getter
    private IMentionDao mentionDao;

    @Bean
    private Jdbi jdbi() throws Exception {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://localhost:3306/bungeeappdb?useSSL=false", "root", "roottoor");
        jdbi.installPlugin(new SqlObjectPlugin());
        this.userDao = jdbi.onDemand(IUserDao.class);
        this.postDao = jdbi.onDemand(IPostDao.class);
        this.userFollowingsDao = jdbi.onDemand(IUserFollowingsDao.class);
        this.mentionDao = jdbi.onDemand(IMentionDao.class);
        return jdbi;
    }

}