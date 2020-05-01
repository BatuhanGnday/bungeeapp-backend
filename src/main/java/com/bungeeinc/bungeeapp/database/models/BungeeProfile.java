package com.bungeeinc.bungeeapp.database.models;

import lombok.Data;
import lombok.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

@Data
public class BungeeProfile {

    int id;

    @NonNull
    int userId;

    String nickname;

    String biography;

    boolean isPrivate;

    String email;

    String bannerKey;

    String profileImageKey;

    Date birthday;

    public static class Mapper implements ColumnMapper<BungeeProfile> {

        @Override
        public BungeeProfile map(ResultSet r, int columnNumber, StatementContext ctx) throws SQLException {
            int id = r.getInt("id");
            int userId = r.getInt("user_id");
            String nickname = r.getString("nickname");
            String biography = r.getString("biography");
            boolean isPrivate = r.getBoolean("private");
            String email = r.getString("email");
            String bannerKey = r.getString("banner_key");
            String profileImageKey = r.getString("profile_image_key");
            Date birthday = r.getDate("birthday");

            BungeeProfile profile = new BungeeProfile(userId);
            profile.setId(id);
            profile.setNickname(nickname);
            profile.setBiography(biography);
            profile.setPrivate(isPrivate);
            profile.setEmail(email);
            profile.setBannerKey(bannerKey);
            profile.setProfileImageKey(profileImageKey);
            profile.setBirthday(birthday);

            return profile;
        }
    }


}
