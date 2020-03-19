package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.request.FollowRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.followrequest.GetFollowRequestResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.FollowingUserResponseModel;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.GetFollowersResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.getfollowers.response.GetFollowersResponseType;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
     *
     * @param request FollowRequest
     * @return follow type response
     */
    public FollowResponse follow(FollowRequest request, User user) {

        // UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());
        // User user = (User)token.getPrincipal();

        User followingUser = databaseService.getUserDao().getById(request.getFollowingUserId());

        if (databaseService.getUserDao().isFollow(user.getId(), request.getFollowingUserId())) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        if (databaseService.getUserDao().getById(request.getFollowingUserId()).isPrivate()) {
            databaseService.getUserDao().follow(user.getId(), followingUser.getId(), Boolean.FALSE);
            return new FollowResponse(FollowResponseType.SUCCESS);
        }
        databaseService.getUserDao().follow(user.getId(), request.getFollowingUserId(), Boolean.TRUE);
        return new FollowResponse(FollowResponseType.SUCCESS);
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

        int numberOfFollowing = databaseService.getUserDao().numberOfFollowers(id);
        String biography = databaseService.getUserDao().getBiography(id);
        boolean isFollowed = databaseService.getUserDao().isFollow(user.getId(), viewedUser.getId());
        boolean blocked_by_viewer = false;
        boolean country_block = false;
        int numberOfFollowed = databaseService.getUserDao().numberOfFollowed(id);
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

    public GetFollowersResponse getFollowers(User activeUser, int id) {
        List<User> userList = databaseService.getUserDao().getFollowers(id);
        List<FollowingUserResponseModel> responseModelList = new ArrayList<>();

        for (User user : userList) {
            int userId = user.getId();
            String username = user.getUsername();
            String fullName = user.getFirstName() + " " + user.getLastName();
            String imageKey = user.getImageKey();
            boolean isFollowedByActiveUser = databaseService.getUserDao().isFollow(activeUser.getId(), user.getId());
            responseModelList.add(new FollowingUserResponseModel(userId, username, fullName, imageKey, isFollowedByActiveUser));
        }

        return new GetFollowersResponse(responseModelList, GetFollowersResponseType.SUCCESSFUL);
    }

    public GetFollowRequestResponse getFollowRequests(User user) {
        List<User> requestList = new ArrayList<>(databaseService.getUserDao().getFollowRequests(user.getId()));
        List<FollowingUserResponseModel> followingUserResponseModels = new ArrayList<>();

        for (User user1 : requestList) {
            int userId = user1.getId();
            String username = user1.getUsername();
            String fullName = user1.getFirstName() + " " + user1.getLastName();
            String imageKey = user1.getImageKey();
            boolean isFollowedByActiveUser = databaseService.getUserDao().isFollow(user.getId(), user1.getId());
            followingUserResponseModels.add(new FollowingUserResponseModel(userId,
                    username, fullName, imageKey, isFollowedByActiveUser));
        }
        return new GetFollowRequestResponse(followingUserResponseModels);
    }

}
