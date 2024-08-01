package com.bugata.oauth.jwt.repositories;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bugata.oauth.jwt.helpers.RefreshableCRUDRepository;
import com.bugata.oauth.jwt.models.RefreshToken;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@Repository
public interface RefreshTokenRepository extends RefreshableCRUDRepository<RefreshToken, Integer> {

    Optional<RefreshToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query(value = "DELETE r1 FROM OAUTH_REFRESH_TOKENS r1 " +
                   "JOIN OAUTH_REFRESH_TOKENS r2 ON r1.expiry_date <= r2.expiry_date " +
                   "WHERE r2.token = :token", nativeQuery = true)
    void deleteByExpiryDateLessThanAndTokenEquals(String token);
}
