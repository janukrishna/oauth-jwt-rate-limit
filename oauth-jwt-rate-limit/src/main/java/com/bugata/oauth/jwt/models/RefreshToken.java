package com.bugata.oauth.jwt.models;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */

@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
@Table(name = "OAUTH_REFRESH_TOKENS")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String token;

    private Instant expiryDate;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "username")
    private UserInfo userInfo;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Instant getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Instant expiryDate) {
		this.expiryDate = expiryDate;
	}

	public UserInfo getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
    
	 public static Builder builder() {
	        return new Builder();
	    }

	    public static class Builder {
	        private String token;
	        private Instant expiryDate;
	        private UserInfo userInfo;

	        public Builder userInfo(UserInfo userInfo) {
	            this.userInfo = userInfo;
	            return this;
	        }

	        public Builder token(String token) {
	            this.token = token;
	            return this;
	        }

	        public Builder expiryDate(Instant expiryDate) {
	            this.expiryDate = expiryDate;
	            return this;
	        }

	        public RefreshToken build() {
	            return new RefreshToken(0, token, expiryDate, userInfo); // id will be auto-generated
	        }
	    }

		public RefreshToken(int id, String token, Instant expiryDate, UserInfo userInfo) {
			super();
			this.id = id;
			this.token = token;
			this.expiryDate = expiryDate;
			this.userInfo = userInfo;
		}

		public RefreshToken() {
			super();
			// TODO Auto-generated constructor stub
		}
		
		
	    
	    
}
