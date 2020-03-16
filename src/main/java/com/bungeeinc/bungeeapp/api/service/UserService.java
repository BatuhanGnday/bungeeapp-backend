package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.request.FollowRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.follow.response.FollowResponseType;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedList;
import java.util.List;

@Service
public class UserService {

    private final DatabaseService databaseService;
    private JwtTokenUtil tokenUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    @Autowired
    public UserService(DatabaseService databaseService, HttpServletRequest servletRequest) {
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
    public FollowResponse follow(FollowRequest request) {

        UsernamePasswordAuthenticationToken token = ((UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication());
        User user = (User)token.getPrincipal();

        if (databaseService.getUserDao().isFollow(user.getId(), request.getFollowingUserId())) {
            return new FollowResponse(FollowResponseType.FAILED);
        }
        databaseService.getUserDao().follow(user.getId(), request.getFollowingUserId());
        return new FollowResponse(FollowResponseType.SUCCESS);
    }

    /**
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

    public ProfileResponse getProfile(int id) {
        User user = getById(id);

        int numberOfFollowing = databaseService.getUserDao().numberOfFollowers(id);
        String biography = "Superbus solitudo foris attrahendams galatae est. " +
                "Flavum, primus parss cito desiderium de nobilis, fatalis bromium.";
        boolean blocked_by_viewer = false;
        boolean country_block = false;
        int numberOfFollowed = databaseService.getUserDao().numberOfFollowed(id);
        String fullName = user.getFirstName() + " " + user.getLastName();
        boolean isJoinedRecently = false;
        boolean isPrivate = false;
        boolean isVerified = true;
        String profilePicImageKey = "/bungee/profile/image.jpg";
        String username = user.getUsername();
        List<Post> featuredPosts = new LinkedList<>();

        return new ProfileResponse(biography,
                blocked_by_viewer,country_block,numberOfFollowing,numberOfFollowed,
                fullName,id,isJoinedRecently,isPrivate,isVerified,profilePicImageKey,
                username,featuredPosts);


    }
}
