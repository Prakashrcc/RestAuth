package com.pks.demo.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="users")
public class RestUser {
	@Id
	private String username;
	private String password;
	private boolean enabled;
	private String Token;
	
	
	public RestUser() {
		super();
	}
	public RestUser(String username, String password, boolean enabled, String token) {
		super();
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		Token = token;
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
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public String getToken() {
		return Token;
	}
	public void setToken(String token) {
		Token = token;
	}
	
	

}
