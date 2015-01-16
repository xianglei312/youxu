package com.concordy.pro.bean;

import java.io.Serializable;
import java.util.Date;

public class User implements Serializable{
	private String userId;
	private String roles;
	private String username;
	private String password;
	private String confirmPassword;
	private String access_token;
	private	String grant_type;
	private long expires;
	private String email;
	private String phone;
	private Date last_access_time;
	private String description;
	public String getRoles() {
		return roles;
	}
	public void setRoles(String roles) {
		this.roles = roles;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGrant_type() {
		return grant_type;
	}
	public void setGrant_type(String grant_type) {
		this.grant_type = grant_type;
	}
	public User(){}
	public User(String username, String password, String grant_type) {
		super();
		this.username = username;
		this.password = password;
		this.grant_type = grant_type;
	}
	public User(String username, String password) {
		this.username = username;
		this.password = password;
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
	public String getAccessToken() {
		return access_token;
	}
	public void setAccessToken(String access_token) {
		this.access_token = access_token;
	}
	public long getExpires() {
		return expires;
	}
	public void setExpires(long expires) {
		this.expires = expires;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public Date getLast_access_time() {
		return last_access_time;
	}
	public void setLast_access_time(Date last_access_time) {
		this.last_access_time = last_access_time;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@Override
	public String toString() {
		return "User [userId=" + userId + ", username=" + username
				+ ", password=" + password + ", access_token=" + access_token
				+ ", expires=" + expires + "]";
	}
	
}
