package com.bungeeinc.bungeeapp;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.bungeeinc.bungeeapp.database.dao.ITagDao;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BungeeAPIApplication {
    public static void main(String[] args) {
        SpringApplication.run(BungeeAPIApplication.class, args);

    }
}
