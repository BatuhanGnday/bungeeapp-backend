package com.bungeeinc.bungeeapp.database;

import com.bungeeinc.bungeeapp.database.dao.*;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class DatabaseService {

    @Getter
    private IAccountDao accountDao;

    @Getter
    private IPostDao postDao;

    @Getter
    private IUserFollowingsDao userFollowingsDao;

    @Getter
    private IMentionDao mentionDao;

    @Getter
    private IUserBlocksDao userBlocksDao;

    @Bean
    private Jdbi jdbi() throws Exception {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://64.227.118.33:3306/bungeeappdb?useSSL=false", "root", "bungeeappdb");
        jdbi.installPlugin(new SqlObjectPlugin());
        this.accountDao = jdbi.onDemand(IAccountDao.class);
        this.postDao = jdbi.onDemand(IPostDao.class);
        this.userFollowingsDao = jdbi.onDemand(IUserFollowingsDao.class);
        this.mentionDao = jdbi.onDemand(IMentionDao.class);
        this.userBlocksDao = jdbi.onDemand(IUserBlocksDao.class);
        return jdbi;
    }

}