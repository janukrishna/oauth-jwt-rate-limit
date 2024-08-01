package com.bugata.oauth.jwt.services;

import java.util.List;

import com.bugata.oauth.jwt.dtos.UserRequest;
import com.bugata.oauth.jwt.dtos.UserResponse;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
public interface UserService {

    UserResponse saveUser(UserRequest userRequest);

    UserResponse getUser();

    List<UserResponse> getAllUser();


}
