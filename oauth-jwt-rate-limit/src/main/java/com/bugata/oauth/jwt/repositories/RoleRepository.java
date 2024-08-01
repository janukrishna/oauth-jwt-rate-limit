package com.bugata.oauth.jwt.repositories;

import org.springframework.stereotype.Repository;

import com.bugata.oauth.jwt.helpers.RefreshableCRUDRepository;
import com.bugata.oauth.jwt.models.UserRole;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@Repository
public interface RoleRepository extends RefreshableCRUDRepository<UserRole, Long> {



}
