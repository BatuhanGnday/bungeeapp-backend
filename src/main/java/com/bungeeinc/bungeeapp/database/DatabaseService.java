package com.bungeeinc.bungeeapp.database;

import com.bungeeinc.bungeeapp.database.dao.*;
import lombok.Getter;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;

@Service
public class DatabaseService {

    @Autowired
    public DatabaseService(@Qualifier("dataSource") DataSource dataSource) {
        Jdbi jdbi = Jdbi.create(dataSource);
        jdbi.installPlugin(new SqlObjectPlugin());
        this.accountDao = jdbi.onDemand(IAccountDao.class);
        this.profileDao = jdbi.onDemand(IProfileDao.class);
        this.tagDao = jdbi.onDemand(ITagDao.class);
        this.postDao = jdbi.onDemand(IPostDao.class);
        this.userFollowingsDao = jdbi.onDemand(IUserFollowingsDao.class);
        this.mentionDao = jdbi.onDemand(IMentionDao.class);
        this.userBlocksDao = jdbi.onDemand(IUserBlocksDao.class);
        this.postImageDao = jdbi.onDemand(IPostImageDao.class);
    }

    @Getter
    private IAccountDao accountDao;

    @Getter
    private IPostDao postDao;

    @Getter
    private IProfileDao profileDao;

    @Getter
    private IUserFollowingsDao userFollowingsDao;

    @Getter
    private IMentionDao mentionDao;

    @Getter
    private IUserBlocksDao userBlocksDao;

    @Getter
    private ITagDao tagDao;

    @Getter
    private IPostImageDao postImageDao;

}