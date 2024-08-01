package com.bugata.oauth.jwt.dtos;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public class JwtResponseDTO {

    private String accessToken;
    private String token;
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
    
	  public static Builder builder() {
	        return new Builder();
	    }

	    public static class Builder {
	        private String accessToken;
	        private String token;

	        public Builder accessToken(String accessToken) {
	            this.accessToken = accessToken;
	            return this;
	        }

	        public Builder token(String token) {
	            this.token = token;
	            return this;
	        }

	        public JwtResponseDTO build() {
	            return new JwtResponseDTO(accessToken, token);
	        }
	    }

		public JwtResponseDTO(String accessToken, String token) {
			super();
			this.accessToken = accessToken;
			this.token = token;
		}
	    
	    
    
    
}
