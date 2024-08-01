package com.bugata.oauth.jwt.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Bugata
 * Created By Krishna on 01-07-2024
 * @project oauth-jwt
 */
@Entity
@Table(name = "OAUTH_USERS")
public class UserInfo implements java.io.Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private long id;
    
	//@Id
	@Column(name = "username", unique = true, nullable = false)
    private String username;
    @JsonIgnore
    @Column(name = "password", nullable = false, length = 4000)
    private String password;
    
   // @ManyToMany(fetch = FetchType.EAGER)
    //@OneToMany(fetch = FetchType.EAGER)
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "userInfo")
	private Set<UserRole> roles = new HashSet<UserRole>(0);

    
    
	public long getId() {
		return id;
	}
	public void setId(long id) {
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
