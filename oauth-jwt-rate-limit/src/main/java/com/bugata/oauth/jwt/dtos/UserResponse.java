package com.bugata.oauth.jwt.dtos;

import java.util.Set;

import com.bugata.oauth.jwt.models.UserRole;
import com.fasterxml.jackson.annotation.JsonBackReference;
/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
public class UserResponse {

    private Long id;
    private String username;
    @JsonBackReference
    private Set<UserRole> roles;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public Set<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}

}
