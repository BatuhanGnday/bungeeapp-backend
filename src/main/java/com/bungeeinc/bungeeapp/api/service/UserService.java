package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.UserModelSummary;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.login.response.LoginResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.profile.response.ProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.register.response.RegisterResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.Post;
import com.bungeeinc.bungeeapp.database.models.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final DatabaseService databaseService;
    private JwtTokenUtil tokenUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AuthenticationManager authenticationManager;

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
/*

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        if (authentication == null) {
            return new LoginResponse(LoginResponseType.USER_NOT_EXIST, null);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = databaseService.getUserDao().findByUsername(request.getUsername());
        String token = tokenUtil.generateToken(user);

        return new LoginResponse(LoginResponseType.SUCCESS, token);
*/
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

    public User getByUsername(String username) {
        return this.databaseService.getUserDao().findByUsername(username);
    }


    public User getById(int id) {
        return this.databaseService.getUserDao().getById(id);
    }


    public ProfileResponse getProfile(User user, int id) {

        User viewedUser = databaseService.getUserDao().getById(id);

        int numberOfFollowing = databaseService.getUserFollowingsDao().numberOfFollowers(id);
        String biography = databaseService.getUserDao().getBiography(id);
        boolean isFollowed = databaseService.getUserFollowingsDao().isFollow(user.getId(), viewedUser.getId());
        boolean blocked_by_viewer = false;
        boolean country_block = false;
        int numberOfFollowed = databaseService.getUserFollowingsDao().numberOfFollowed(id);
        String fullName = viewedUser.getFirstName() + " " + viewedUser.getLastName();
        boolean isJoinedRecently = false;

        // Date object to find current time
        Date currentTime = new Date();

        // Time values
        long createdOnLong = viewedUser.getCreatedOn().getTime();
        long currentTimeLong = currentTime.getTime();

        // Finds the difference between createdOn time and currentTime
        // Checks if it's been 3 days
        if (currentTimeLong - createdOnLong < 259000000L) {
            isJoinedRecently = true;
        }

        boolean isPrivate = false;
        boolean isVerified = true;
        String profilePicImageKey = "/bungee/profile/image.jpg";
        String username = user.getUsername();
        List<Post> featuredPosts = new LinkedList<>();

        return new ProfileResponse(biography,
                blocked_by_viewer,country_block,numberOfFollowing, isFollowed, numberOfFollowed,
                fullName,id,isJoinedRecently,isPrivate,isVerified,profilePicImageKey,
                username,featuredPosts);


    }

    private List<UserModelSummary> userToFollowingUserResponseModel(User activeUser, List<User> userList) {

        if (userList.isEmpty()) {
            return Collections.emptyList();
        }

        List<UserModelSummary> responseModelList = new ArrayList<>();
        for(User user : userList) {
            int id = user.getId();
            String username = user.getUsername();
            String fullName = user.getFirstName() + " " + user.getLastName();
            String imageKey = user.getImageKey();
            boolean isFollowedByActiveUser = databaseService.getUserFollowingsDao()
                    .isFollow(activeUser.getId(), user.getId());
            responseModelList.add(new
                    UserModelSummary(id, username, fullName, imageKey, isFollowedByActiveUser));
        }
        return responseModelList;
    }

}
