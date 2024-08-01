package com.bugata.oauth.jwt.dtos;

import java.util.Set;

import com.bugata.oauth.jwt.models.UserRole;
/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@ToString
public class UserRequest {

    private Long id;
    private String username;
    private String password;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<UserRole> getRoles() {
		return roles;
	}
	public void setRoles(Set<UserRole> roles) {
		this.roles = roles;
	}
    
    


}
