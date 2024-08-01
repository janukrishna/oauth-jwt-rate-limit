package com.bugata.oauth.jwt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import com.bugata.oauth.jwt.helpers.RefreshableCRUDRepositoryImpl;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@EnableJpaRepositories(repositoryBaseClass = RefreshableCRUDRepositoryImpl.class)
@SpringBootApplication
public class OauthJwtApplication {

    public static void main(String[] args) {
        SpringApplication.run(OauthJwtApplication.class, args);
    }

}
