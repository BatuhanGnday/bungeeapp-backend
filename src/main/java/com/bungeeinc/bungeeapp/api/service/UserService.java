package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.response.LoginResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.show.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.response.RegisterResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.request.UpdateProfileRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.response.UpdateProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.response.UpdateProfileResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Post;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.*;

@Service
public class UserService {

    private final DatabaseService databaseService;
    private JwtTokenUtil tokenUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.tokenUtil = new JwtTokenUtil();
    }

    /**
     *
     * @param request LoginRequest
     * @return login response with a token if success
     */
    public LoginResponse auth(LoginRequest request) {
        User user = databaseService.getUserDao().findByUsername(request.getUsername());
        if (user == null) {
            return new LoginResponse(LoginResponseType.USER_NOT_EXIST, null);
        }
        if(bCryptPasswordEncoder.matches(request.getPassword(), user.getPassword())){
            return new LoginResponse(LoginResponseType.SUCCESS, tokenUtil.generateToken(user));
        }
        return new LoginResponse(LoginResponseType.PASSWORD_FAIL,null);
    }

    /**
     * Register a user
     *
     * @param request RegisterRequest
     * @return RegisterResponse
     */
    public RegisterResponse register(RegisterRequest request) {
        User user = new User(
                request.getUsername(),
                bCryptPasswordEncoder.encode(request.getPassword()),
                request.getFirstName(),
                request.getLastName(),
                request.getEmail()
        );

        if (this.databaseService.getUserDao().isExistByUsernameOrEmail(user)) {
            return new RegisterResponse(RegisterResponseType.USERNAME_OR_EMAIL_EXISTS);
        }
        user.setId(
                this.databaseService.getUserDao().createUser(user)
        );
        return new RegisterResponse(RegisterResponseType.SUCCESS);
    }

    /**
     *
     * @param user Active User
     * @param id
     * @return
     */
    public ProfileResponse showProfile(User user, int id) {

        User viewedUser = databaseService.getUserDao().getById(id);

        int numberOfFollowing = databaseService.getUserFollowingsDao().numberOfFollowers(id);
        String biography = viewedUser.getBiography();
        boolean isFollowed = databaseService.getUserFollowingsDao().isFollow(user.getId(), viewedUser.getId());
        boolean blocked_by_viewer = false;
        boolean country_block = false;
        int numberOfFollowed = databaseService.getUserFollowingsDao().numberOfFollowed(id);
        String fullName = viewedUser.getFirstName() + " " + viewedUser.getLastName();

        // Date object to find current time
        Date currentTime = new Date();

        // Time values
        long createdOnLong = viewedUser.getCreatedOn().getTime();
        long currentTimeLong = currentTime.getTime();

        // Finds the difference between createdOn time and currentTime
        // Checks if it's been 3 days
        boolean isJoinedRecently = (currentTimeLong - createdOnLong < 259000000L);

        boolean isPrivate = viewedUser.isPrivate();
        boolean isVerified = true;
        String profilePicImageKey = "/bungee/profile/image.jpg";
        String username = viewedUser.getUsername();
        List<Post> featuredPosts = new LinkedList<>(databaseService.getPostDao().getByUserId(id));

        return new ProfileResponse(biography,
                blocked_by_viewer,country_block,numberOfFollowing, isFollowed, numberOfFollowed,
                fullName,id,isJoinedRecently,isPrivate,isVerified,profilePicImageKey,
                username,featuredPosts);


    }

    public UpdateProfileResponse updateUser(User user, UpdateProfileRequest request) {

        if (user == null) {
            return new UpdateProfileResponse(UpdateProfileResponseType.FAILED);
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setBiography(request.getBiography());
        user.setAge(request.getAge());
        user.setImageKey(request.getImageKey());
        user.setPrivate(request.isPrivate());

        return new UpdateProfileResponse(UpdateProfileResponseType.SUCCESS);
    }
}
