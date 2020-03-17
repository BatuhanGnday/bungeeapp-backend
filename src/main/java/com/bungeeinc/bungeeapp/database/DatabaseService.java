package com.bungeeinc.bungeeapp.database;

import com.bungeeinc.bungeeapp.database.dao.IPostDao;
import com.bungeeinc.bungeeapp.database.dao.IUserDao;
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

    @Bean
    private Jdbi jdbi() throws Exception {
        Jdbi jdbi = Jdbi.create("jdbc:mysql://localhost:3306/bungeeappdb?useSSL=false", "root", "roottoor");
        jdbi.installPlugin(new SqlObjectPlugin());
        this.userDao = jdbi.onDemand(IUserDao.class);
        this.postDao = jdbi.onDemand(IPostDao.class);
        return jdbi;
    }

}