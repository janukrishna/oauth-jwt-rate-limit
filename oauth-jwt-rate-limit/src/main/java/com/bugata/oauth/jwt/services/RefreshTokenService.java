package com.bugata.oauth.jwt.services;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bugata.oauth.jwt.helpers.UserDetailsServiceImpl;
import com.bugata.oauth.jwt.models.RefreshToken;
import com.bugata.oauth.jwt.repositories.RefreshTokenRepository;
import com.bugata.oauth.jwt.repositories.UserRepository;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */

@Service
public class RefreshTokenService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    UserRepository userRepository;
    
    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .userInfo(userRepository.findByUsername(username))
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(600000))
               //   .expiryDate(Instant.now().plusMillis(1000*60*1)) //System.currentTimeMillis()+1000*60*1
                .build();
        return refreshTokenRepository.save(refreshToken);
    }



    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token){
        if(token.getExpiryDate().compareTo(Instant.now())<0){
        	refreshTokenRepository.deleteByExpiryDateLessThanAndTokenEquals(token.getToken());
        //    refreshTokenRepository.delete(token);
            logger.error(token.getToken() + " Refresh token is expired. Please make a new login..!",new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!"));
            throw new RuntimeException(token.getToken() + " Refresh token is expired. Please make a new login..!");
        }
        return token;

    }

}
