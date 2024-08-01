package com.bugata.oauth.jwt.dtos;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class RefreshTokenRequestDTO {
    private String token;

	public String getToken() { 
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
    
    
}
