package com.bungeeinc.bungeeapp.api.service;

import com.bungeeinc.bungeeapp.api.service.jwtconfig.JwtTokenUtil;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.request.LoginRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.response.LoginResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.auth.response.LoginResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.request.RegisterRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.response.RegisterResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.signup.response.RegisterResponseType;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.request.UpdateAccountRequest;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.response.UpdateProfileResponse;
import com.bungeeinc.bungeeapp.api.service.model.endpoint.user.update.response.UpdateProfileResponseType;
import com.bungeeinc.bungeeapp.database.DatabaseService;
import com.bungeeinc.bungeeapp.database.models.account.BungeeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    private final DatabaseService databaseService;
    private JwtTokenUtil tokenUtil;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public AccountService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.tokenUtil = new JwtTokenUtil();
    }

    /**
     *
     * @param request LoginRequest
     * @return login response with a token if success
     */
    public LoginResponse login(LoginRequest request) {
        BungeeUserDetails user = databaseService.getAccountDao().findByUsername(request.getUsername());
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
        BungeeUserDetails user = new BungeeUserDetails(
                request.getUsername(),
                bCryptPasswordEncoder.encode(request.getPassword())
        );

        if (this.databaseService.getAccountDao().isExistByUsername(user)) {
            return new RegisterResponse(RegisterResponseType.USERNAME_OR_EMAIL_EXISTS);
        }
        user.setId(
                this.databaseService.getAccountDao().createUser(user)
        );
        return new RegisterResponse(RegisterResponseType.SUCCESS);
    }

    public UpdateProfileResponse updateUser(BungeeUserDetails user, UpdateAccountRequest request) {

        if (user == null) {
            return new UpdateProfileResponse(UpdateProfileResponseType.FAILED);
        }

        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setEnabled(request.getEnabled());
        user.setDeleted(request.getDeleted());

        return new UpdateProfileResponse(UpdateProfileResponseType.SUCCESS);
    }
}
