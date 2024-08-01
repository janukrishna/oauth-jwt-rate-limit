package com.bugata.oauth.jwt.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bugata.oauth.jwt.dtos.AuthRequestDTO;
import com.bugata.oauth.jwt.dtos.JwtResponseDTO;
import com.bugata.oauth.jwt.dtos.RefreshTokenRequestDTO;
import com.bugata.oauth.jwt.dtos.UserRequest;
import com.bugata.oauth.jwt.dtos.UserResponse;
import com.bugata.oauth.jwt.models.RefreshToken;
import com.bugata.oauth.jwt.services.JwtService;
import com.bugata.oauth.jwt.services.RefreshTokenService;
import com.bugata.oauth.jwt.services.UserService;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@RestController
@RequestMapping("/api/v1")
public class UserController {
	
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    RefreshTokenService refreshTokenService;


    @Autowired
    private  AuthenticationManager authenticationManager;

    @PostMapping(value = "/save")
  //  @PostMapping(value = "/save", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity saveUser(@RequestBody UserRequest userRequest) {
        try {
            UserResponse userResponse = userService.saveUser(userRequest);
            logger.info("User saved successfully: {}", userRequest.getUsername());
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
        	 logger.error("Error saving user: {}", userRequest.getUsername(), e);
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/users")
    public ResponseEntity getAllUsers() {
        try {
            List<UserResponse> userResponses = userService.getAllUser();
            logger.info("Fetched {} users", userResponses.size());
            return ResponseEntity.ok(userResponses);
        } catch (Exception e){
        	 logger.error("Error fetching users", e);
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/profile")
    public ResponseEntity<UserResponse> getUserProfile() {
        try {
        UserResponse userResponse = userService.getUser();
        logger.info("User profile fetched successfully: {}", userResponse.getUsername());
        
        return ResponseEntity.ok().body(userResponse);
        } catch (Exception e){
        	 logger.error("Error fetching user profile", e);
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/test")
    public String test() {
        try {
            return "Welcome";
        } catch (Exception e){
        	 logger.error("Error executing test method", e);
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/login")
    public JwtResponseDTO AuthenticateAndGetToken(@RequestBody AuthRequestDTO authRequestDTO){
        logger.info("Authenticating user: {}", authRequestDTO.getUsername());
    	Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequestDTO.getUsername(), authRequestDTO.getPassword()));
        if(authentication.isAuthenticated()){
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(authRequestDTO.getUsername());
            logger.info("User authenticated successfully: {}", authRequestDTO.getUsername());
            return JwtResponseDTO.builder()
                   .accessToken(jwtService.GenerateToken(authRequestDTO.getUsername()))
                   .token(refreshToken.getToken()).build();

        } else {
            logger.error("Authentication failed for user: {}", authRequestDTO.getUsername());
            throw new UsernameNotFoundException("invalid user request..!!");
        }

    }


    @PostMapping("/refreshToken")
    public JwtResponseDTO refreshToken(@RequestBody RefreshTokenRequestDTO refreshTokenRequestDTO){
    	try {
    	return refreshTokenService.findByToken(refreshTokenRequestDTO.getToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUserInfo)
                .map(userInfo -> {
                    String accessToken = jwtService.GenerateToken(userInfo.getUsername());
                    logger.info("Token refreshed successfully: {}", refreshTokenRequestDTO.getToken());

                    return JwtResponseDTO.builder()
                            .accessToken(accessToken)
                            .token(refreshTokenRequestDTO.getToken()).build();
                }).orElseThrow(() ->new RuntimeException("Refresh Token is not in DB..!!"));
    } catch (Exception e) {
        logger.error("Error refreshing token: {}", refreshTokenRequestDTO.getToken(), e);
        throw new RuntimeException(e);
    }
    }

}
